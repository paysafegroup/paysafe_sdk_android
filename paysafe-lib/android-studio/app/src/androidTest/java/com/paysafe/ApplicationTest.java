package com.paysafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.SingleUseToken;
import com.paysafe.utils.Constants;
import com.paysafe.utils.Utils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    // Paysafe Api Client Object
    private static PaysafeApiClient client;

    // Credentials
    private static String merchantApiKey;
    private static String merchantApiPassword;

    // Merchant Account Number
    private static String merchantAccountNumber;
    private static Context appContext;

    @BeforeClass
    public static void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();
        getConfigurationProperties();
        //assertEquals("com.example.demoapp", appContext.getPackageName());
    }

    @BeforeClass
    public static void getConfigurationProperties() {
        try {
            merchantApiKey = Utils.getProperty("merchant-api-key", appContext);
            merchantApiPassword = Utils.getProperty("merchant-api-password", appContext);
            merchantAccountNumber = Utils.getProperty("merchant-account-number", appContext);
        } catch (IOException ioExp) {
            Utils.debugLog("APPLICATION TEST: IOException: " + ioExp.getMessage());
        }
    } // end of getConfigurationProperties()

    /**
     * Test Case to Create Single Use Token With All Fields.
     */
    @Test

    public void testCreateSingleUseToken() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

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
            //assertEquals("Single Use Token should have an id", sObjResponse.getId());
            assertNotNull("Single Use Token should have an id", sObjResponse.getId());
            Gson gson = new Gson();
            String Response_Json = gson.toJson(sObjResponse); //convert
            System.out.println(Response_Json);


        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testCreateSingleUseToken()

    /**
     * Test Case to Create Google Pay Token
     */
    @Test
    public void testGooglePayToken() {
        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);
        System.out.println("Started the test case for android google pay token ");
        android.util.Log.d( "k","Started the test case for android google pay token ");
        try {
            SingleUseToken sObjResponse = client.customerVaultService()
                    .createGooglePayPaymentToken(
                            SingleUseToken.builder()
                                    .googlePayPaymentToken()
                                    .signature("MEQCIBlsjYNJK6xDE8SjJGXCpb47qxr0rVMW2+VI6fCE/YURAiBIp8INbvpuptpBsd9EKJxoV20uVmYZDHDbHxh9zx5Ibg==")
                                    .protocolVersion("ECv1")
                                    .signedMessage("\n" +
                                            "{\"encryptedMessage\":\"ICtGHtIuBT6XzH13Cn+r7O/V2mJcvAzADiBEtBQD2ltYlGsKIya23VxoubNFY43tOQxFkVMko1TKGdl9AYuS3bMdyva801gJg4LFvehbVAFC3bxiXMeRRovphV1w3l03sSv4FAS1qQJxYCGHTjsuabca1qZyTyZ6T3Up1IMwRNLzHoJL+Zhb96Xdqt3BeoGCBwo4qt+pgt2CZ/K2Wl2NMkJ+8mEsV7FbIXZfwXCp5BTfldT3vMECoVLkqbbb8A0YOkKU3qbAMRAdgAEhs9Rbr3jbCnjLA9AwvXCn1iScks9uL3wvMdcvq9hTD/wqANU9R1MncT8oIdEm0GCbbDd1zZt6xSVs/mDCO6ITuEH4OUggf9asZJ3rghvkvhnXdKhlFT9s5kH+rz6YsnYnTrgvFxfM4o9v2iQL1q2KvBpfQpXk+F4JZEEpNYVpiA9WvKQmNB8Z\",\"ephemeralPublicKey\":\"BAImEaaaGFxwqKPb6QVzgHRsXP3+slhyDSR/A4xMwv40M+0ueLzNjm3XJmPj8o84ZhnmZdDKdFnbPlE5mLZdC/Y\\u003d\",\"tag\":\"VJcytuFfWY8FRylAqV3YWYu4gZtwkMH010pUjijZHak\\u003d\"}")  .done()
                                    .build());

            Gson gson = new Gson();
            String Response_Json = gson.toJson(sObjResponse); //convert
            System.out.println("Response from google pay token" +Response_Json);

            assertNotNull("Payment Token should have an id", sObjResponse.getId());

        } catch (IOException ioExp) {
            // Log IO Exception
                if(Constants.DEBUG_LOG_VALUE)
                    Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    }

    /**
     * Test Case to Create Single Use Token With missing Billing Address Zip Code.
     */
    @Test
    public void testMissingBillingAddressZipCode() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

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
                            .done()
                            .done()
                            .build());


            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("billingAddress.zip",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("may not be empty",
                    sObjResponse.getError().getFieldErrors().get(0).getError());
            Gson gson = new Gson();
            String Response_Json = gson.toJson(sObjResponse); //convert
            System.out.println(Response_Json);
        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingBillingAddressZipCode()

    /**
     * Test Case to Create Single Use Token with Missing Billing Address Details.
     */
    @Test
    public void testMissingBillingAddressDetails() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

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
                            .done()
                            .build());

            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("card.billingAddress",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("Missing billing address details",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingBillingAddressDetails()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Details.
     */
    public void testMissingCardExpiryDetails() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
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

            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("card.cardExpiry",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("may not be null",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardExpiryDetails()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Month Field.
     */
    public void testMissingCardExpiryMonth() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
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

            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("card.cardExpiry.month",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("must be greater than or equal to 1",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardExpiryMonth()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Year Field.
     */
    public void testMissingCardExpiryYear() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
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

            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("card.cardExpiry.year",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("must be greater than or equal to 1900",
                    sObjResponse.getError().getFieldErrors().get(0).getError());
            assertEquals("cardExpiry",
                    sObjResponse.getError().getFieldErrors().get(1).getField());
            assertEquals("Card expired",
                    sObjResponse.getError().getFieldErrors().get(1).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardExpiryYear()

    /**
     * Test Case to Create Single Use Token with Expired Card.
     */
    public void testCreateSingleUseTokenWithExpiredCard() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
                            .year(2015)
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

            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("cardExpiry",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("Card expired",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testCreateSingleUseTokenWithExpiredCard()

    /**
     * Test Case to Create Single Use Token with Missing Card Number.
     */
    public void testMissingCardNumber() {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
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

            assertEquals("5068", sObjResponse.getError().getCode());
            assertEquals("cardNum",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            assertEquals("Invalid Value",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardNumber()
}