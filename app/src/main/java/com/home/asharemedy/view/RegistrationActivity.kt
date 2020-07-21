package com.home.asharemedy.view

import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.utils.Utils
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.custom_action_bar.*

class RegistrationActivity : BaseActivity() {

    private var utilityID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        try {
            txtCABtitle.text = getString(R.string.registration_title_new)
            imgCABback.setOnClickListener {
                finish()
            }

            getUtilityList(false, txtUtilityName)

            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Utils.isRegisterSuccess)
            finish()
    }

    private fun clickPerform() {
        try {
            r_lyt_utility.setOnClickListener {
                /*if (UtilitiesData.getCount() > 0) {
                    openDialog(getString(R.string.select_utility), txtUtilityName)
                } else {
                    getUtilityList(true, txtUtilityName)

                }*/
            }

            /*imgUtility.setOnClickListener {
                showInfoMessage("Your Account Number is located next to the ACCOUNT NUMBER label, at the top right ACCOUNT INFORMATION section of the EOCWD Bill.")
            }*/
            /*imgAcc.setOnClickListener {
                showInfoMessage("Your Account Number is located next to the ACCOUNT NUMBER label, at the top right ACCOUNT INFORMATION section of the EOCWD Bill.")
            }
            imgMeter.setOnClickListener {
                showInfoMessage("Your Meter Number is located next to the METER NUMBER label, at the top right ACCOUNT INFORMATION section of the EOCWD Bill.")
            }
            imgSerciveZip.setOnClickListener {
                showInfoMessage("Service Zip Code is the zip code of the premise.")
            }*/
            /*imgEmail.setOnClickListener {
                showInfoMessage("Your Account Number is located next to the ACCOUNT NUMBER label, at the top right ACCOUNT INFORMATION section of the EOCWD Bill.")
            }*/


            btnSubmitRegister.setOnClickListener {
                validationField()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showInfoMessage(infoMessage: String) {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(infoMessage)

        alertDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    private fun validationField() {
        try {
            var allValid = true
            if (editName.text!!.isEmpty()) {
                showSuccessPopup("Please enter Name")
                !allValid
                return
            } else if (editMobileNumber.text!!.isEmpty() || editMobileNumber.length() < 10) {
                showSuccessPopup("Please enter Mobile Number")
                !allValid
                return
            } else if (editEmail.text!!.isEmpty()) {
                showSuccessPopup(getString(R.string.error_message_enter_email))
                !allValid
                return
            } else if (!editEmail.text!!.isEmpty() && !Utils.isValidEmail(editEmail.text.toString().trim())) {
                showSuccessPopup("Please enter valid Email")
                !allValid
                return
            }
            else if (txtUtilityName.text!!.isEmpty()) {
                showSuccessPopup("Please select Country")
                !allValid
                return
            }
            else if (editPassword.text!!.isEmpty() || !isPasswordValid(editPassword.text)) {
                showSuccessPopup(getString(R.string.password_validation_message))
                !allValid
                return
            } else if (editCPassword.text!!.isEmpty() || !isPasswordValid(editCPassword.text)) {
                showSuccessPopup("Please enter Confirm Password")
                !allValid
                return
            } else if (editCPassword!!.text!!.isNotEmpty() && editPassword!!.text!!.isNotEmpty() &&
                editPassword.text.toString() != editCPassword.text.toString()
            ) {
                showSuccessPopup("Password doesn't match")
                !allValid
                return
            } else if (allValid) {
                /*val intent =
                    Intent(this@SignupStepOneActivity, SignupStepTwoActivity::class.java)
                intent.putExtra(ApiUrls.CustomerUtilityId, utilityID)
                intent.putExtra(ApiUrls.AccountNumber, editAccountNo.text.toString())
                intent.putExtra(ApiUrls.MeterNumber, editMeterNo.text.toString())
                intent.putExtra(ApiUrls.ServiceZipCode, editServiceZipCode.text.toString())
                intent.putExtra(ApiUrls.EmailID, editEmail.text.toString())
                intent.putExtra(ApiUrls.Password, editPassword.text.toString())

                startActivity(intent)*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUtilityList(dialogOpen: Boolean = false, textView: TextView) =
        if (Utils.isConnected(this)) {
//        showDialog()
            /*try {
                val apiService =
                    ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
                val call = apiService.getUtilityList(*//*ApiUrls.AuthKey*//*)
                call.enqueue(object : Callback<ResponseModelClasses.UtilityListResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModelClasses.UtilityListResponseModel>,
                        response: Response<ResponseModelClasses.UtilityListResponseModel>
                    ) {
                        dismissDialog()
                        try {
                            if (response.body() != null)
                                UtilitiesData.clearArrayList()
                            UtilitiesData.addArrayList(response.body()!!.Results.Table)
                            if (dialogOpen) {
                                openDialog(getString(R.string.select_utility), textView)
                            }
                            AppLog.printLog("UtilityList Response- ", response.body().toString())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseModelClasses.UtilityListResponseModel>,
                        t: Throwable
                    ) {
                        try {
                            dismissDialog()
                            AppLog.printLog("Failure()- ", t.message.toString())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
//            dismissDialog()
            }*/
        } else {
            showToast(getString(R.string.internet))
        }

    private fun openDialog(title: String, textView: TextView) {
        /*try {
            val dialog = Dialog(this@RegistrationActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = title

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager
            val itemDecor = DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
            dialog.dialogRecycleView.addItemDecoration(itemDecor)
            val mAdapter = UtilitiesListAdapter() { position ->
                val data = UtilitiesData.getArrayItem(position)
                utilityID = data.UtilityId.toString()
                textView.text = data.Name
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

    }
}