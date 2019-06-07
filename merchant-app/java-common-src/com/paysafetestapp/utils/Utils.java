package com.paysafetestapp.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;

public class Utils {
    // progress Dialog
    private static ProgressDialog mProgressDialog;

    /**
     * Check whether or not the String is empty.
     *
     * @param source String source.
     * @return Boolean value to check whether or not the string is empty.
     */
    public static boolean isEmpty(String source) {
        return (null == source || source.trim().equals(""));
    } // end ff isEmpty()

    /**
     * Checks Network Availability.
     *
     * @param context Context.
     * @return Boolean value to check whether or not Network is available.
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean isInternetAvailable = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();

            //noinspection deprecation
            if (networkInfo != null && (networkInfo.isConnected())
                    && networkInfo.isAvailable()) {
                isInternetAvailable = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isInternetAvailable;
    } // end of isNetworkAvailable()

    /**
     * Checks NFC Availability.
     *
     * @param context Context.
     * @return Boolean value to check whether or not NFC is available.
     */
    public static boolean isNFCAvailable(Context context) {
        boolean isNFCAvailable = false;

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            isNFCAvailable = true;
        }

        return isNFCAvailable;
    } // end of isNFCAvailable()

    /**
     * This Method uses ConnectivityManager to checks if connectivity exists or is in the process
     * of being established.
     * But it will not guarantee the instant availability.
     *
     * @param context Context.
     * @return Boolean value to check whether or not connectivity is available.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //noinspection deprecation
        return netInfo != null && netInfo.isConnectedOrConnecting();
    } // end of isOnline()

    /**
     * Start Progress Dialog.
     * Displays process dialog when the application is communicating to the server or
     * processing some transactions.
     */
    public static void startProgressDialog(Context context, String message) {
        mProgressDialog = ProgressDialog.show(context, "", message);
    } // end of startProgressDialog()

    /**
     * Stop Progress Dialog.
     * Process dialog stops when on-going process is completed.
     */
    public static void stopProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;

        }
    } // end of stopProgressDialog()

    /**
     * This method is used for get twelve alphanumeric random number
     *
     * @return String
     */
    public static String twelveDigitRandomAlphanumeric() {
        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(60, random).toString(32).toUpperCase();
    } // end of twelveDigitRandomAlphanumeric

    /**
     * Show Alert Dialog.
     *
     * @param alertMessage Alter message to be displayed on the Alert Dialog.
     * @param context      Context.
     */
    public static void showDialogAlert(String alertMessage, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    } // end of showDialogAlert()

    /**
     * Debugger Logs
     *
     * @param msg Message to Log
     */
    public static void debugLog(String msg) {
        if (Constants.DEBUG_LOG_VALUE) {
            android.util.Log.v(Constants.TAG_LOG, msg);
        }
    } // end of debugLog()

    /**
     * Get properties from assets/config.properties file.
     *
     * @param key     Alter message to be displayed on the Alert Dialog.
     * @param context Context.
     * @return String
     */
    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    } // end of getProperty()
} // end of class Utils
