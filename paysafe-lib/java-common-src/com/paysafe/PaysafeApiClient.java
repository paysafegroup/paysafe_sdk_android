/*
 * Copyright (c) 2016 Paysafe
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 */

package com.paysafe;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paysafe.common.ApiException;
import com.paysafe.common.EntityNotFoundException;
import com.paysafe.common.Id;
import com.paysafe.common.InvalidCredentialsException;
import com.paysafe.common.InvalidRequestException;
import com.paysafe.common.PaysafeException;
import com.paysafe.common.PermissionException;
import com.paysafe.common.RequestConflictException;
import com.paysafe.common.RequestDeclinedException;
import com.paysafe.common.impl.BaseDomainObject;
import com.paysafe.common.impl.IdAdapter;
import com.paysafe.common.impl.Request;
import com.paysafe.customervault.CustomerVaultService;
import com.paysafe.utils.Constants;
import com.paysafe.utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * The Class PaysafeApiClient.
 * 
 * @author asawari.vaidya
 * @since 26-06-2015.
 */
public class PaysafeApiClient {

    // Credentials
    private final String apiEndPoint;
    private final String keyId;
    private final String keyPassword;
    private String accountNumber;

	// Customer Vault Service object
	private CustomerVaultService customerVaultService;
	
	// Set the timeout in milliseconds until a connection is established.
	// The default value is zero, that means the timeout is not used.
	private static final int TIMEOUT_CONNECTION = 10 * 1000;

	// Set the default read timeout
	// in milliseconds which is the timeout for waiting for data.
	private static final int READ_TIMEOUT = 10 * 1000;

	// The gson object used to deserialize the api response.
	private final Gson gsonDeserializer;

    /**
     * Constructor 1
     *
     * @param keyId Api Key
     * @param keyPassword Api Password
     * @param environment Environment(TEST/LIVE)
     * */
    public PaysafeApiClient(
            final String keyId,
            final String keyPassword,
            final Environment environment) {

        this.keyId = keyId;
        this.keyPassword = keyPassword;

        apiEndPoint = environment.getUrl();
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(Id.class,
                new IdAdapter());
        gsonDeserializer = gsonBuilder.create();
    } // end of Constructor

    /**
     * Constructor 2
     *
     * @param keyId Api Key
     * @param keyPassword Api Password
     * @param environment Environment(TEST/LIVE)
     * @param accountNumber Merchant Account Number
     * */
    public PaysafeApiClient(
            final String keyId,
            final String keyPassword,
            final Environment environment,
            final String accountNumber) {

        this(keyId, keyPassword, environment);
        this.setAccount(accountNumber);

    } // end of Constructor

    /**
     * Set Account Number
     *
     * @param accountNumber Merchant Account Number
     * */
    private void setAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    } // end of setAccount()

    /**
     * Get Account Number
     *
     * @return Merchant Account Number
     * */
    public final String getAccount() {
        return accountNumber;
    } // end of getAccount()

    /**
     * Customer Vault Service
     *
     * @return Customer Vault Service object
     * */
    public final CustomerVaultService customerVaultService() {
        if (null == customerVaultService) {
            customerVaultService = new CustomerVaultService(this);
        }
        return customerVaultService;
    } // end of customerVaultService()
    
    /**
     * Process error.
     *
     * @param code the response code
     * @param  obj object to embed in the exception
     * @param cause the original exception
     * @return the exception
     */
    private PaysafeException getException(
            final int code,
            final BaseDomainObject obj,
            final IOException cause) {
      switch (code) {
        case 400:
          return new InvalidRequestException(obj, cause);
        case 401:
          return new InvalidCredentialsException(obj, cause);
        case 402:
          return new RequestDeclinedException(obj, cause);
        case 403:
          return new PermissionException(obj, cause);
        case 404:
          return new EntityNotFoundException(obj, cause);
        case 409:
          return new RequestConflictException(obj, cause);
        case 406:
        case 415:
        default:
          return new ApiException(obj, cause);
      }
    }// end of getException()
    
    
    /**
     * Create a connection from a request and return a specified type.
     *
     * @param <T> an extension of BaseDomainObject
     * @param request the Request object to be processed
     * @param returnType the class that will be returned
     * @return desc
     * @throws IOException desc
     * @throws PaysafeException desc
     */
  
    public final <T extends BaseDomainObject> T processRequest(
            final Request request,
            Class<T> returnType)
            throws IOException, PaysafeException {
       
        final URL url = new URL(request.buildUrl(apiEndPoint));

        // LOG
        if(Constants.DEBUG_LOG_VALUE)
            Utils.debugLog("PAYSAFE API CLIENT: Beta URL.");

        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // LOG
        if(Constants.DEBUG_LOG_VALUE)
            Utils.debugLog("PAYSAFE API CLIENT: HTTPS Connection established successfully.");

        try {
            connection.setConnectTimeout(TIMEOUT_CONNECTION);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.addRequestProperty("Authorization", "Basic " + getAuthenticatedString());
            connection.setRequestMethod(request.getMethod().toString());
            connection.addRequestProperty("Content-Type", "application/json");
            
            // Write to the stream if we can
            if (request.getMethod().equals(Request.RequestType.POST)
                    || (request.getMethod().equals(Request.RequestType.PUT))) {
              connection.setDoOutput(true);

         

            final OutputStream os = connection.getOutputStream();
            final DataOutputStream dos = new DataOutputStream(os);
            try {
                dos.write(serializeObject(request, returnType).getBytes("UTF-8"));
                dos.flush();
              } finally {
                dos.close();
                os.close();
              }
            }

            return getReturnObject(connection, returnType);
          } finally {
            connection.disconnect();
         // LOG
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("PAYSAFE API CLIENT: HTTPS Connection Disconnected.");
          }
        }  // end of processRequest()
      
    /**
     * Get the return object back from the connection.
     *
     * @param <T> an extension of BaseDomainObject
     * @param connection HttpsURLConnection
     * @param returnType the class that will be returned
     * @return
     * @throws IOException
     * @throws PaysafeException
     */
  	private <T extends BaseDomainObject> T getReturnObject(HttpsURLConnection connection, Class<T> returnType)
  			throws IOException, PaysafeException {
  		try {
  			InputStream is = connection.getInputStream();

  			try {
  				return deserializeStream(is, returnType);
  			} finally {
  				is.close();
  			}
  		} catch (IOException cause) {
  			// store the cause so we know to throw an exception after parsing
  			// the response
  			InputStream is = connection.getErrorStream();
  			try {
  				throw getException(connection.getResponseCode(), deserializeStream(is, returnType), cause);
  			} finally {
  				is.close();
  			}
  		}
  	}// end of getReturnObject()

    /**
     * Take an input stream and return the gson deserialized version.
     *
     * @param <T> an extension of BaseDomainObject
     * @param is the input stream
     * @param returnType the class that will be returned
     * @return
     * @throws IOException
     */
    private <T extends BaseDomainObject> T deserializeStream(
            InputStream is,
            Class<T> returnType)
            throws IOException {
      final InputStreamReader isr = new InputStreamReader(is, "UTF-8");
      try {
        return gsonDeserializer.fromJson(isr, returnType);
      } finally {
        isr.close();
      }
    }//end of deserializeStream

    /**
     * Take a domain object, and json serialize it.
     *
     * @param request,returnType
     * @return json encoding of the request object
     */
    private String serializeObject(Request request, Class<?> returnType)  {
      final GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.excludeFieldsWithoutExposeAnnotation();
      gsonBuilder.registerTypeHierarchyAdapter(Id.class,new IdAdapter());
      if (null != request.getSerializer()) {
        gsonBuilder.registerTypeAdapter(returnType, request.getSerializer());
      }
      final Gson gson = gsonBuilder.create();
      return gson.toJson(request.getBody());
    }//end of serializeObject()


    /**
     * Gets the Base64 Encoded Authenticated string.
     *
     * @return Base64 Encoded Authentication String
     * @throws IOException
     * */
    private String getAuthenticatedString() throws IOException {

        return Base64.encodeToString((keyId + ':' + keyPassword).getBytes("UTF-8"),
                Base64.NO_WRAP);
    } // end of getAuthenticatedString()

} // end of class PaysafeApiClient
