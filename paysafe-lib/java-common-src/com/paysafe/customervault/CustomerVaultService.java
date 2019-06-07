/*
 * Copyright (c) 2015 Optimal Payments
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.paysafe.customervault;

import java.io.IOException;

import com.paysafe.PaysafeApiClient;
import com.paysafe.common.PaysafeException;
import com.paysafe.common.impl.Request;

/**
 * The Class CustomerVaultService.
 * 
 * @author asawari.vaidya
 * @since 26-06-2015.
 */

public class CustomerVaultService {

    // End point values for API's URL
    private static final String URI = "customervault/v1";
    private static final String SINGLE_USE_TOKEN_PATH  = "/singleusetokens";
    private static final String PAY_WITH_GOOGLE_PATH = "/googlepaysingleusetokens";

    // Object of Class PaysafeApiClient
    private final PaysafeApiClient client;

    /**
    * Constructor
    *
    * @param client Paysafe Api Client object.
    * */
    public CustomerVaultService(final PaysafeApiClient client) {
        this.client = client;
    } // end of Constructor

    /**
     * Create SingleUseToken
     *
     * @param singleUseToken Single Use Token object.
     * @return SingleUseToken object
     * @throws IOException desc
     * @throws PaysafeException desc
     * */
    public final SingleUseToken createSingleUseToken(
            final SingleUseToken singleUseToken)
            throws IOException, PaysafeException {

        // Build request for API
        final Request request = Request.builder()
                .uri(prepareUri(URI + SINGLE_USE_TOKEN_PATH))
                .method(Request.RequestType.POST)
                .body(singleUseToken)
                .build();

        return (client.processRequest(request,SingleUseToken.class));
    } // end of createSingleUseToken()


    /**
     * Create Google Pay Token
     */
    public final SingleUseToken createGooglePayPaymentToken(
            final SingleUseToken singleUseToken)
            throws IOException, PaysafeException {

        // Build request for API
        final Request request = Request.builder()
                .uri(prepareUri(URI + PAY_WITH_GOOGLE_PATH))
                .method(Request.RequestType.POST)
                .body(singleUseToken)
                .build();

        return (client.processRequest(request, SingleUseToken.class));
    } // end of createGooglePayPaymentToken()

    /**
     * Prepare Uri
     *
     * @param path Api Endpoint Path
     * @return Url
     * @throws PaysafeException desc
     * */
    private String prepareUri(final String path) throws PaysafeException {
        if(null == client.getAccount()) {
            throw new PaysafeException("Missing or Invalid Account.");
        }
        return path;
    } // end of prepareUri()

} // end of class CustomerVaultService
