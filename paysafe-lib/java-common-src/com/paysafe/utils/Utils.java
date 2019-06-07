package com.paysafe.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by asawari.vaidya on 17-05-2017.
 */

public class Utils {

    /**
     * Debugger Logs
     * @param msg Message to Log
     */
    public static void debugLog(String msg) {
        if(Constants.DEBUG_LOG_VALUE) {
            android.util.Log.v(Constants.TAG_LOG, msg);
        }
    } // end of debugLog()

    /**
     * Get properties from assets/config.properties file.
     *
     * @param key Alter message to be displayed on the Alert Dialog.
     * @param context Context.
     * @return String
     */
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    } // end of getProperty()

} // end of class Utils
