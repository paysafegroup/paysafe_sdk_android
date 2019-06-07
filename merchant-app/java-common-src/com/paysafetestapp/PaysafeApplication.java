package com.paysafetestapp;

/**
 * The Class PaysafeApplication.
 *
 * @author manisha.rani
 * @since 26-06-2015
 */

import android.app.Application;
import android.content.Context;

public class PaysafeApplication extends Application {
    public static Context mApplicationContext;

    /**
     * On Create.
     */
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
    } // end of onCreate()
} // end of class PaysafeApplication