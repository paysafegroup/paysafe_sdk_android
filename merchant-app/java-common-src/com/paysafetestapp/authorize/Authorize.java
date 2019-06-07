package com.paysafetestapp.authorize;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paysafetestapp.Checkout;
import com.paysafetestapp.PaysafeApplication;
import com.paysafetestapp.R;
import com.paysafetestapp.connection.HttpsServerConnection;
import com.paysafetestapp.utils.Constants;
import com.paysafetestapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by asawari.vaidya on 02-05-2017.
 */

public class Authorize extends Activity {

    // EditText
    private EditText mMerchantReferenceNumberEditText;
    private EditText mAmountEditText;
    private EditText mPaymentTokenEditText;

    // Context
    private Context mContext;
    private HttpsServerConnection mServer_conn;

    // String
    private String mMerchantRefNo;
    private String mAmount;
    private String mPaymentToken;
    private String mPaymentMethod;

    // Configuration
    private String merchantApiKeyIdAuthorize;
    private String merchantApiKeyPasswordAuthorize;
    private String merchantAccountNumberAuthorize;

    /**
     * On Create Activity.
     * @param savedInstanceState Object of Bundle holding instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorize);
        init();

        // Set values for Merchant Account
        setMerchantAccount(mPaymentMethod);
    }

    /**
     * This method is called when initialize UI
     */
    private void init() {
        mContext = this;
        mServer_conn = new HttpsServerConnection();

        // Button
        Button mAuthorizeButton = findViewById(R.id.btn_authorize);
        Button mBackButton = findViewById(R.id.btn_back);

        mMerchantReferenceNumberEditText = findViewById(R.id.et_merchantref);
        mAmountEditText = findViewById(R.id.et_amount);
        mPaymentTokenEditText = findViewById(R.id.et_payment_token);

        mBackButton.setOnClickListener(mClickListener);
        mAuthorizeButton.setOnClickListener(mClickListener);

        Intent intentPaymentToken = getIntent();
        String mMerchantRefNo = Utils.twelveDigitRandomAlphanumeric();
        if (!Utils.isEmpty(mMerchantRefNo)) {
            mMerchantReferenceNumberEditText.setText(mMerchantRefNo);
        }
        mPaymentTokenEditText.setText(intentPaymentToken.getStringExtra("PwgPaymentToken"));
        mPaymentMethod = intentPaymentToken.getStringExtra("PwgPaymentMethod");

    } // end of init()

    /**
     * This method is called when button click listener
     */
    private final View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    final Intent intent = new Intent(Authorize.this, Checkout.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_authorize:
                    buttonAuthorizeClick();
                    break;

                default:
                    break;
            }
        }
    }; // end of onClickListener

    /**
     * This method is used to execute async task
     */
    private void buttonAuthorizeClick(){
        getValuesFromEditText();

        if (Utils.isEmpty(mAmount)) {
            Utils.showDialogAlert(Constants.PLEASE_ENTER_AMOUNT, mContext);
            return;
        }
        if (Utils.isEmpty(mPaymentToken)) {
            Utils.showDialogAlert(Constants.PLEASE_ENTER_PAYMENT_TOKEN,
                    mContext);
            return;
        }

        //Check Internet connection
        if (!isCheckInternet()) {
            Toast.makeText(getApplicationContext(),	Constants.PLEASE_TURN_ON_YOUR_INTERNET, Toast.LENGTH_LONG).show();
            return;
        }

        String urlString = String.format(
                getResources().getString(R.string.url_authorize),
                merchantAccountNumberAuthorize);

        new AuthorizeRequestViaAsyncTask().execute(urlString);

    } // end of buttonAuthorizeClick()

    /**
     * This method is used to get values from edit text
     *
     *
     */
    private void getValuesFromEditText() {
        mMerchantRefNo = mMerchantReferenceNumberEditText.getText().toString();
        mAmount = mAmountEditText.getText().toString();
        mPaymentToken = mPaymentTokenEditText.getText().toString();
    } // end of getValuesFromEditTest()

    /**
     * Set Merchant Account from Payment Method
     */
    private void setMerchantAccount(String mPaymentMethod) {

        try {
            merchantApiKeyIdAuthorize = Utils.getProperty("merchant_api_key_id_auth", mContext);
            merchantApiKeyPasswordAuthorize = Utils.getProperty("merchant_api_key_password_auth", mContext);

            switch (mPaymentMethod) {
                case "TOKENIZED_CARD":
                    merchantAccountNumberAuthorize = Utils.getProperty(
                            "merchant_account_number_tokenized_card", mContext);
                    break;

                case "CARD_ON_FILE":
                    merchantAccountNumberAuthorize = Utils.getProperty(
                            "merchant_account_number_card_on_file", mContext);
                    break;
            }
        } catch(IOException ioExp) {
            Utils.showDialogAlert("IOException: "+ ioExp.getMessage(), mContext);
        }

    } // end of setMerchantAccount()

    // Async task for Authorize and create profile

    class Wrapper {
        String responseString;
        String url;
    }

    @SuppressLint("StaticFieldLeak")
    public class AuthorizeRequestViaAsyncTask extends AsyncTask<String, String, Wrapper> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.startProgressDialog(Authorize.this,
                    getString(R.string.loading_text));
        }

        @Override
        protected Wrapper doInBackground(String... params) {
            String url = params[0];
            String responseString;
            Wrapper wrapper;
            responseString = sendRequestToserver(url);
            wrapper = new Wrapper();
            wrapper.responseString = responseString;
            wrapper.url = url;

            return wrapper;
        }

        protected void onPostExecute(Wrapper wrapper) {
            Utils.stopProgressDialog();
            if (wrapper != null && !Utils.isEmpty(wrapper.responseString)) {
                if (wrapper.responseString.contains(Constants.CONNECTION_REFUSED)) {
                    Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET, mContext);
                    return;
                }
            }
            if (wrapper != null
                    && String.format(
                    getResources().getString(R.string.url_authorize),
                    merchantAccountNumberAuthorize)
                    .equalsIgnoreCase(wrapper.url)) {
                parseAuthorize(mServer_conn, wrapper.responseString);
            }
        }
    }

    /**
     * This method is called when send request to server
     *
     * @return String
     */
    private String sendRequestToserver(String url) {
        HttpsServerConnection httpsServerConnection = new HttpsServerConnection();
        String responseString = null;
        getValuesFromEditText();
        String base64EncodedCredentials;
        try {
            if (String.format(getResources().getString(R.string.url_authorize),
                    merchantAccountNumberAuthorize).equalsIgnoreCase(url)) {

                base64EncodedCredentials = Base64
                        .encodeToString((merchantApiKeyIdAuthorize + ":" + merchantApiKeyPasswordAuthorize)
                                .getBytes("UTF-8"), Base64.NO_WRAP);

                responseString = httpsServerConnection.requestUrl(
                        base64EncodedCredentials, url,
                        createAuthorizeJsonObject(), Constants.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }

    /**
     * This method is called when parse the authorize response
     *
     * @return void
     *
     */
    private void parseAuthorize(HttpsServerConnection server_conn,
                                String responseString) {

        // Parse Json Object
        final ArrayList<String> responseParams = new ArrayList<>();
        responseParams.add("id");
        if (!Utils.isEmpty(responseString) && responseString.contains("error")) {
            responseParams.add("error");
            @SuppressWarnings("unchecked")
            final Map<String, Object> map = server_conn.readAndParseJSON1(
                    responseString, responseParams);
            try {
                final String errorDetails = (String) map.get("error");
                final ArrayList<String> responseParamsData = new ArrayList<>();
                responseParamsData.add("code");
                responseParamsData.add("message");
                @SuppressWarnings("unchecked")
                final Map<String, Object> map1 = server_conn.readAndParseJSON1(
                        errorDetails, responseParamsData);
                String strCode = (String) map1.get("code");
                String strMessage = (String) map1.get("message");

                Utils.showDialogAlert(strCode + ": " + strMessage, mContext);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String strStatus = checkedAuthorizeStatus(responseParams,server_conn, responseString);
            if (!Utils.isEmpty(strStatus) && strStatus.equals("COMPLETED")) {
                showDialogAlert(Constants.AUTHORIZATION_SUCCESSFUL,mContext);
            }

        }
    }

    /**
     * This method is used for create authorize JsonObject
     *
     * @return JSONObject
     *
     *
     */
    private JSONObject createAuthorizeJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantRefNum", mMerchantRefNo);
            json.put("amount", mAmount);
            json.put("settleWithAuth", false);
            JSONObject card = new JSONObject();
            card.put("paymentToken", mPaymentToken);

            json.put("description", "Video purchase");
            json.put("customerIp", "204.91.0.12");
            json.put("card", card);

            JSONObject billingDetails = new JSONObject();

            billingDetails.put("zip","M5H 2N2");
            json.put("billingDetails",billingDetails);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String checkedAuthorizeStatus(ArrayList<String> responseParams,
                                          HttpsServerConnection server_conn, String responseString) {
        responseParams.add("status");
        @SuppressWarnings("unchecked")
        Map<String, Object> map = server_conn.readAndParseJSON(responseString,
                responseParams);

        return (String) map.get("status");
    }

    /**
     * This method is used to show alert dialog
     *
     * @return void
     */
    private void showDialogAlert(String alertMessage, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Authorize.this, Checkout.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    /**
     * This method is called when check the Internet
     *
     * @return void
     *
     */

    private boolean isCheckInternet() {
        boolean isNetworkAvailable = Utils
                .isNetworkAvailable(PaysafeApplication.mApplicationContext);
        boolean isOnline = Utils
                .isOnline(PaysafeApplication.mApplicationContext);
        if (isNetworkAvailable) {
            return true;
        } else return isOnline;
    }


} // end of class Authorize