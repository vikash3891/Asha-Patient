package com.home.asharemedy.payu;

import com.home.asharemedy.utils.Constants;

public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            return Constants.MERCHANT_KEY;
        }

        @Override
        public String merchant_ID() {
            return "5960507";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return Constants.MERCHANT_SALT;
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return Constants.MERCHANT_KEY;
        }
        @Override
        public String merchant_ID() {
            return "5960507";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return Constants.MERCHANT_SALT;
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
