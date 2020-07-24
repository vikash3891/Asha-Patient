package com.home.asharemedy.payu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.home.asharemedy.R;

public class MerchantCheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_checkout);

        TextView postParamsTextView = (TextView) findViewById(R.id.text_view_post_params);

        //post data received by this activity contains all params posted to webview in transaction request.
        String postData = getIntent().getStringExtra("postData");
        postParamsTextView.setText("Merchant's post data : "+postData);

    }
}