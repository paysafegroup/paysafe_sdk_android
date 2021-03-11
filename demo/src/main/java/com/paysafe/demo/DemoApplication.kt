/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.paysafe.Environment
import com.paysafe.customervault.CustomerVaultService
import com.paysafe.customervault.customerVaultService
import com.paysafe.paysafeApiClient
import com.paysafe.threedsecure.ThreeDSecureService
import com.paysafe.threedsecure.threeDSecureService
import com.paysafe.threedsecure.ui.uiStyle


class DemoApplication : Application() {

    lateinit var threeDSecureService: ThreeDSecureService
        private set

    lateinit var customerVaultService: CustomerVaultService
        private set

    override fun onCreate() {
        super.onCreate()

        installSecurityProvider(this)

        val client3DS = initializeAPiClient3DS()
        val clientCV = initializeAPiClientCV()

        val style = initializeUiStyle()

        threeDSecureService = threeDSecureService {
            context = applicationContext
            apiClient = client3DS
            uiStyle = style
        }
        customerVaultService = customerVaultService {
            apiClient = clientCV
        }
    }

    private fun installSecurityProvider(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                ProviderInstaller.installIfNeeded(context)
            } catch (e: GooglePlayServicesRepairableException) {
                GoogleApiAvailability.getInstance()
                    .showErrorNotification(context, e.connectionStatusCode)
            } catch (e: GooglePlayServicesNotAvailableException) {
                Log.e(
                    DemoApplication::class.java.simpleName,
                    "Google Play services not available due to error " + e.errorCode
                )
            }
        }
    }

    private fun initializeAPiClient3DS() = paysafeApiClient {
        apiKey = MERCHANT_API_KEY_ID_SBOX
        apiSecret = MERCHANT_API_KEY_PASSWORD_SBOX
        accountId = MERCHANT_ACCOUNT_NUMBER_SBOX
        environment = Environment.TEST
        httpLoggingEnabled = true
    }

    private fun initializeAPiClientCV() = paysafeApiClient {
        apiKey = MERCHANT_API_KEY_ID_CV
        apiSecret = MERCHANT_API_KEY_PASSWORD_CV
        accountId = MERCHANT_ACCOUNT_NUMBER_CV
        environment = Environment.TEST
        httpLoggingEnabled = true
    }

    private fun initializeUiStyle() = uiStyle {
        toolbarStyle {
            textColor = ContextCompat.getColor(applicationContext, R.color.colorBlack)
            textFontName = Typeface.DEFAULT.toString()
            textFontSize = 16
            backgroundColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
            buttonText = "Back"
            headerText = "Secure 3DS 2.0 Payment"
        }
        labelStyle {
            textColor = ContextCompat.getColor(applicationContext, R.color.colorBlack)
            textFontName = Typeface.DEFAULT.toString()
            textFontSize = 16
            headingTextColor = ContextCompat.getColor(applicationContext, R.color.colorBlack)
            headingTextFontName = Typeface.DEFAULT.toString()
            headingTextFontSize = 20
        }
        verifyButtonStyle {
            textColor = ContextCompat.getColor(applicationContext, R.color.colorWhite)
            textFontName = Typeface.DEFAULT.toString()
            textFontSize = 16
            backgroundColor = ContextCompat.getColor(applicationContext, R.color.colorAccent)
            cornerRadius = 32
        }
        resendButtonStyle {
            textColor = ContextCompat.getColor(applicationContext, R.color.colorWhite)
            textFontName = Typeface.DEFAULT.toString()
            textFontSize = 16
            backgroundColor = ContextCompat.getColor(applicationContext, R.color.colorAccent)
            cornerRadius = 32
        }
    }

    companion object {
        //3DS credentials
        private const val MERCHANT_ACCOUNT_NUMBER_SBOX = "<account-number>"
        private const val MERCHANT_API_KEY_ID_SBOX = "<api-key-id>"
        private const val MERCHANT_API_KEY_PASSWORD_SBOX =
            "<api-key-password>"
        //Customer Vault credentials
        private const val MERCHANT_ACCOUNT_NUMBER_CV = "<account-number>"
        private const val MERCHANT_API_KEY_ID_CV = "<api-key-id>"
        private const val MERCHANT_API_KEY_PASSWORD_CV =
            "<api-key-password>"
    }

}