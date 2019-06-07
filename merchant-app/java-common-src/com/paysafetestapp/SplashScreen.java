package com.paysafetestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;

import com.paysafetestapp.utils.Constants;

/**
 * The Class SplashScreen.
 *
 * @author manisha.rani
 * @since 26-06-2015
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } catch (Exception ex) {
            Log.e("While StrictMode", ex.toString());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Checkout.class);
                startActivity(intent);
                finish();

            }
        }, Constants.SPLASH_TIME_OUT);
    }
}