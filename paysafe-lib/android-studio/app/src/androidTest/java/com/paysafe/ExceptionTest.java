package com.paysafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.SingleUseToken;
import com.paysafe.utils.Constants;
import com.paysafe.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Asawari.Vaidya on 23-09-2015.
 */

@RunWith(AndroidJUnit4.class)
public class ExceptionTest {

    // Paysafe Api Client Object
    private static PaysafeApiClient client;

    // Credentials
    private String merchantApiKey;
    private String merchantApiPassword;

    // Merchant Account Number
    private String merchantAccountNumber;
    private Context appContext;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();
        getConfigurationProperties();
        //assertEquals("com.example.demoapp", appContext.getPackageName());
    }


    private void getConfigurationProperties() {
        try {
            merchantApiKey = Utils.getProperty("merchant-api-key-id", appContext);
            merchantApiPassword = Utils.getProperty("merchant-api-key-password", appContext);
            merchantAccountNumber = Utils.getProperty("merchant-account-number", appContext);
        } catch(IOException ioExp) {
            Utils.debugLog("EXCEPTION TEST: IOException: "+ ioExp.getMessage());
        }
    } // end of getConfigurationProperties()

    /**
     * Test Case to Create Single Use Token with Missing or Invalid Merchant Account Number.
     */
    public void testMissingOrInvalidAccount() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
                            .year(2019)
                            .done()
                            .billingAddress()
                            .street("100 Queen Street West")
                            .street2("Unit 201")
                            .city("Toronto")
                            .country("CA")
                            .state("ON")
                            .zip("M5H 2N2")
                            .done()
                            .done()
                            .build());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            Utils.debugLog("EXCEPTION TEST: Missing or Invalid Account: " + oExp.getMessage());
                    assertNotNull("Missing or Invalid Account.", oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + e.getMessage());
        }
    } // end of testMissingOrInvalidAccount

    /**
     * Test Case to Create Single Use Token with Invalid Authentication Credentials.
     */
    public void testInvalidAuthenticationCredentials() {
        client = new PaysafeApiClient("username", "password", Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
                            .year(2019)
                            .done()
                            .billingAddress()
                            .street("100 Queen Street West")
                            .street2("Unit 201")
                            .city("Toronto")
                            .country("CA")
                            .state("ON")
                            .zip("M5H 2N2")
                            .done()
                            .done()
                            .build());

            assertEquals("5279", sObjResponse.getError().getCode());
            assertEquals("The authentication credentials are invalid.",
                    sObjResponse.getError().getMessage());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + e.getMessage());
        }
    } // end of testInvalidAuthenticationCredentials()
}
