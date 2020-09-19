package com.home.asharemedy.payu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.material.textfield.TextInputLayout;
import com.home.asharemedy.AshaRemedyApp;
import com.home.asharemedy.R;
import com.home.asharemedy.base.BaseActivity;
import com.home.asharemedy.utils.Constants;
import com.home.asharemedy.utils.Utils;
import com.home.asharemedy.view.SuccessActivity;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.payumoney.sdkui.ui.utils.PPConfig;

public class ActivityPayU extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ActivityPayU : ";
    private boolean isDisableExitConfirmation = false;
    private String userMobile, userEmail;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private SharedPreferences userDetailsPreference;
    private EditText emailValue, mobileValue, amountValue;
    private TextView doctorName, doctorSpeciality, address, ailmentValue, dateValue, timeValue;
    private RadioGroup radioGroup_select_env;
    private ImageView imageBack;

    private AppPreference mAppPreference;

    private Button payNowButton;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_payu);
            mAppPreference = new AppPreference();

            doctorName = findViewById(R.id.doctorName);
            doctorSpeciality = findViewById(R.id.doctorSpeciality);
            address = findViewById(R.id.address);
            emailValue = findViewById(R.id.emailValue);
            mobileValue = findViewById(R.id.mobileValue);
            amountValue = findViewById(R.id.amountValue);
            ailmentValue = findViewById(R.id.ailmentValue);
            dateValue = findViewById(R.id.dateValue);
            timeValue = findViewById(R.id.timeValue);
            imageBack = findViewById(R.id.imageBack);

            settings = getSharedPreferences("settings", MODE_PRIVATE);


            doctorName.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedDoctorFacility()).getName());
            doctorSpeciality.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedDoctorFacility()).getSpecialization());
            address.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedDoctorFacility()).getAddress1() + " " +
                    Utils.INSTANCE.getSelectedDoctorFacility().getAddress2());
            mobileValue.setText(Objects.requireNonNull(Utils.INSTANCE.getProfileData()).getPatient_mobile());
            emailValue.setText(Objects.requireNonNull(Utils.INSTANCE.getProfileData()).getPatient_email());
            amountValue.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedDoctorFacility()).getFees());

            ailmentValue.setText(Utils.INSTANCE.getSelectedAilmentOrServiceName());
            dateValue.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedGridList()).getSlot_date());//Utils.INSTANCE.getDate());
            timeValue.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedGridList().getStart_time())
                    + " - " + Objects.requireNonNull(Utils.INSTANCE.getSelectedGridList().getEnd_time()));// Utils.INSTANCE.getTime());
            AppCompatRadioButton radio_btn_sandbox = findViewById(R.id.radio_btn_sandbox);
            AppCompatRadioButton radio_btn_production = findViewById(R.id.radio_btn_production);
            radioGroup_select_env = findViewById(R.id.radio_grp_env);

            payNowButton = findViewById(R.id.pay_now_button);
            payNowButton.setOnClickListener(this);

            initListeners();

            //Set Up SharedPref
            setUpUserDetails();

            if (settings.getBoolean("is_prod_env", false)) {
                ((AshaRemedyApp) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
                radio_btn_production.setChecked(true);
            } else {
                ((AshaRemedyApp) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
                radio_btn_sandbox.setChecked(true);
            }
            setupCitrusConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
            ignored.printStackTrace();
        }
        return hexString.toString();
    }


    public static void setErrorInputLayout(EditText editText, String msg, TextInputLayout textInputLayout) {
        textInputLayout.setError(msg);
        editText.requestFocus();
    }

    public static boolean isValidEmail(String strEmail) {
        return strEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(AppPreference.PHONE_PATTERN);

        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private void setUpUserDetails() {
        try {
            userDetailsPreference = getSharedPreferences(AppPreference.USER_DETAILS, MODE_PRIVATE);
            userEmail = userDetailsPreference.getString(AppPreference.USER_EMAIL, Objects.requireNonNull(Utils.INSTANCE.getProfileData()).getPatient_email());
            userMobile = userDetailsPreference.getString(AppPreference.USER_MOBILE, Objects.requireNonNull(Utils.INSTANCE.getProfileData()).getPatient_mobile());

            amountValue.setText(Objects.requireNonNull(Utils.INSTANCE.getSelectedDoctorFacility()).getFees());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function sets the mode to PRODUCTION in Shared Preference
     */
    private void selectProdEnv() {

        try {
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((AshaRemedyApp) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
                    editor = settings.edit();
                    editor.putBoolean("is_prod_env", true);
                    editor.apply();

                    setupCitrusConfigs();
                }
            }, AppPreference.MENU_DELAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function sets the mode to SANDBOX in Shared Preference
     */
    private void selectSandBoxEnv() {
        try {
            ((AshaRemedyApp) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
            editor = settings.edit();
            editor.putBoolean("is_prod_env", false);
            editor.apply();

            setupCitrusConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCitrusConfigs() {
        try {
            AppEnvironment appEnvironment = ((AshaRemedyApp) getApplication()).getAppEnvironment();
            if (appEnvironment == AppEnvironment.PRODUCTION) {
                Toast.makeText(ActivityPayU.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityPayU.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        try {
            Log.d("ActivityPayU", "request code " + requestCode + " resultcode " + resultCode);
            if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                    null) {
                TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                        .INTENT_EXTRA_TRANSACTION_RESPONSE);

                ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

                // Check which object is non-null
                if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                    if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                        //Success Transaction
                        Intent i = new Intent(getApplicationContext(), SuccessActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        //Failure Transaction
                    }

                    // Response from Payumoney
                    String payuResponse = transactionResponse.getPayuResponse();

                    // Response from SURl and FURL
                    String merchantResponse = transactionResponse.getTransactionDetails();

                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();

                } else if (resultModel != null && resultModel.getError() != null) {
                    Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
                } else {
                    Log.d(TAG, "Both objects are null!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            userEmail = Constants.MERCHANT_EMAIL;
            userMobile = Objects.requireNonNull(Utils.INSTANCE.getSelectedDoctorFacility()).getPhone();

            payNowButton.setEnabled(false);
            launchPayUMoneyFlow();

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        try {

            radioGroup_select_env.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                    switch (i) {
                        case R.id.radio_btn_sandbox:
                            selectSandBoxEnv();
                            break;
                        case R.id.radio_btn_production:
                            selectProdEnv();
                            break;
                        default:
                            selectSandBoxEnv();
                            break;
                    }
                }
            });

            imageBack.setOnClickListener(v -> {
                finish();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        try {
            PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

            //Use this to set your custom text on result screen button
            payUmoneyConfig.setDoneButtonText("Asha");

            //Use this to set your custom title for the activity
            payUmoneyConfig.setPayUmoneyActivityTitle("Asha");

            payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

            PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

            double amount = 0;
            try {
                amount = Double.parseDouble(amountValue.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            String txnId = System.currentTimeMillis() + "";
            //String txnId = "TXNID720431525261327973";
            String phone = Objects.requireNonNull(Utils.INSTANCE.getProfileData()).getPatient_mobile();
            String productName = mAppPreference.getProductInfo();
            String firstName = mAppPreference.getFirstName();
            String email = Objects.requireNonNull(Utils.INSTANCE.getProfileData()).getPatient_email();
            String udf1 = "";
            String udf2 = "";
            String udf3 = "";
            String udf4 = "";
            String udf5 = "";
            String udf6 = "";
            String udf7 = "";
            String udf8 = "";
            String udf9 = "";
            String udf10 = "";

            AppEnvironment appEnvironment = ((AshaRemedyApp) getApplication()).getAppEnvironment();
            builder.setAmount(String.valueOf(amount))
                    .setTxnId(txnId)
                    .setPhone(phone)
                    .setProductName(productName)
                    .setFirstName(firstName)
                    .setEmail(email)
                    .setsUrl(appEnvironment.surl())
                    .setfUrl(appEnvironment.furl())
                    .setUdf1(udf1)
                    .setUdf2(udf2)
                    .setUdf3(udf3)
                    .setUdf4(udf4)
                    .setUdf5(udf5)
                    .setUdf6(udf6)
                    .setUdf7(udf7)
                    .setUdf8(udf8)
                    .setUdf9(udf9)
                    .setUdf10(udf10)
                    .setIsDebug(appEnvironment.debug())
                    .setKey(appEnvironment.merchant_Key())
                    .setMerchantId(appEnvironment.merchant_ID());

            try {
                mPaymentParams = builder.build();

                /*
                 * Hash should always be generated from your server side.
                 * */
                //    generateHashFromServer(mPaymentParams);

                /*            *//**
                 * Do not use below code when going live
                 * Below code is provided to generate hash from sdk.
                 * It is recommended to generate hash from server side only.
                 * */
                mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, ActivityPayU.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, ActivityPayU.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }

            } catch (Exception e) {
                // some exception occurred
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                payNowButton.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        try {
            StringBuilder stringBuilder = new StringBuilder();
            HashMap<String, String> params = paymentParam.getParams();
            stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
            stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

            AppEnvironment appEnvironment = ((AshaRemedyApp) getApplication()).getAppEnvironment();
            stringBuilder.append(appEnvironment.salt());

            String hash = hashCal(stringBuilder.toString());
            paymentParam.setMerchantHash(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentParam;
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        try {
            HashMap<String, String> params = paymentParam.getParams();

            // lets create the post params
            StringBuffer postParamsBuffer = new StringBuffer();
            postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
            postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
            postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
            postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

            String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

            // lets make an api call
            GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
            getHashesFromServerTask.execute(postParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityPayU.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            try {
                progressDialog.dismiss();
                payNowButton.setEnabled(true);

                if (merchantHash.isEmpty() || merchantHash.equals("")) {
                    Toast.makeText(ActivityPayU.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                } else {
                    mPaymentParams.setMerchantHash(merchantHash);

                    if (AppPreference.selectedTheme != -1) {
                        PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, ActivityPayU.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                    } else {
                        PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, ActivityPayU.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class EditTextInputWatcher implements TextWatcher {

        private TextInputLayout textInputLayout;

        EditTextInputWatcher(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (s.toString().length() > 0) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }
}
