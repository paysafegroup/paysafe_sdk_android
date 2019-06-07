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
package com.paysafe.common.impl;


import java.net.URLEncoder;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import com.google.gson.JsonSerializer;
import com.paysafe.common.PaysafeException;

/**
 * The class Request.
 * 
 * @author asawari.vaidya
 * @since 26-06-2015.
 * 
 */
public final class Request {

    private BaseDomainObject body;
    private RequestType method;
    private HashMap<String, String> queryStr;
    private String uri;
    
    @SuppressWarnings("rawtypes")
	private JsonSerializer serializer;

    private Request() {

    } // end of Constructor

    /*
    * Get Body
    *
    * @return: BaseDomainObject Object
    * */
    public final BaseDomainObject getBody() {
        return body;
    } // end of getBody()

    /*
    * Get Method
    *
    * @return: RequestType Object
    * */
    public final RequestType getMethod() {
        return method;
    } // end of getMethod()

    /*
    * Build Uri
    *
    * @param: String
    * @return: String
    * */
    public final String buildUrl(final String apiEndPoint) throws PaysafeException {
        if(null == uri) {
            throw new PaysafeException("You must specify the uri when building this object");
        }
        // If this is a url, lets make sure that it contains our endpoint
        if(uri.contains("://")) {
            if(uri.indexOf(apiEndPoint) != 0) {
                throw new PaysafeException("Unexpected endpoint in url: "
                        + uri
                        + "expected: "
                        + apiEndPoint);
            }
            return uri;
        }
        return apiEndPoint + "/" + uri +
                (null == queryStr || queryStr.isEmpty() ? "" : "?" + buildQueryString());

    } // end of buildUri()

    /*
     * Builds the Query String
     *
     * @return: String
     * @throws: RuntimeException
     * */
    private String buildQueryString() throws RuntimeException {
        final StringBuilder response = new StringBuilder();
        if(null != queryStr) {
            for(java.util.Map.Entry<String, String> entry : queryStr.entrySet()) {
                if(response.length() > 0) {
                    response.append("&");
                }

                try {
                    final String value = entry.getValue();
                    response.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    response.append("=");
                    response.append(null != value? URLEncoder.encode(value, "UTF-8") : "");
                } // end of try block
                catch(UnsupportedEncodingException ex) {
                    throw new RuntimeException("This method requires UTF-8 encoding support", ex);
                } // end of catch block
            }
        }
        return response.toString();
    } // end of buildQueryString()

    /*
     * Allow easy building of request objects.
     * */
    public static RequestBuilder builder() {
        return new RequestBuilder();
    } // end of builder()

	
	@SuppressWarnings("rawtypes")
	public JsonSerializer getSerializer() {
	    return serializer;
	  }
	

    /*
     * RequestBuilder Class
     * */
    public static class RequestBuilder {

        private final Request request = new Request();

        /*
         * Build this request
         * */
        public final Request build() {
            return request;
        } // end of build()

        /*
         * Set the uri
         * */
        public final RequestBuilder uri(final String uri) {
            request.uri = uri;
            return this;
        } // end of uri()

        /*
         * Set the body
         * */
        public final RequestBuilder body(final BaseDomainObject body) {
            request.body = body;
            return this;
        } // end of body()

        /*
         * Set the method
         * */
        public final RequestBuilder method(final RequestType method) {
            request.method = method;
            return this;
        } // end of method()

        /*
         * Set the Query String
         * */
        public final RequestBuilder queryStr(final HashMap<String, String> queryStr) {
            request.queryStr = queryStr;
            return this;
        } // end of queryStr()

    } // end of class RequestBuilder

    /*
     * The Enum RequestType
     * */
    public enum RequestType{
        DELETE,
        GET,
        POST,
        PUT
    }

} // end of class Request

