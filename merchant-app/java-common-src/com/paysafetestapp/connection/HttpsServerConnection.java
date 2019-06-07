package com.paysafetestapp.connection;

/**
 * Created by asawari.vaidya on 02-05-2017.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

import com.paysafetestapp.utils.Constants;

public class HttpsServerConnection {

    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int DATARETRIEVAL_TIMEOUT = 10000;

    /**
     * Create a connection from a Request and return a specified response.
     *
     * @param base64EncodedCredentials Credentials in Base64 format
     * @param url                      URL
     * @param jsonObj                  JSON Object
     * @param requestType              Request Type
     * @return Response
     */
    public String requestUrl(String base64EncodedCredentials, String url, JSONObject jsonObj, String requestType) {

        String requestJsonData = jsonObj.toString();

        disableConnectionReuseIfNecessary();
        HttpsURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpsURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            urlConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            // handle POST parameters
            if (requestJsonData != null) {

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod(requestType);
                urlConnection.setFixedLengthStreamingMode(requestJsonData.getBytes().length);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
                urlConnection.setRequestProperty("Accept-Charset", "utf-8");

                final OutputStream outputStream = urlConnection.getOutputStream();
                final DataOutputStream dataOutPutStream = new DataOutputStream(outputStream);
                try {
                    dataOutPutStream.write(requestJsonData.getBytes("utf-8"));
                    dataOutPutStream.flush();
                } finally {
                    dataOutPutStream.close();
                    outputStream.close();
                }
                return getResponse(urlConnection);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            closeConnection(urlConnection);
            e.printStackTrace();
            return getResponse(urlConnection != null ? urlConnection.getErrorStream() : null);
        } finally {
            closeConnection(urlConnection);
        }

        return null;
    }

    /**
     * Create a connection from a Request and return a specified response.
     *
     * @param base64EncodedCredentials Credentials in Base64 format
     * @param url                      URL
     * @param requestJsonData          Requet JSON Date
     * @param requestType              Request Type
     * @return Response
     */

    public String requestUrl(String base64EncodedCredentials, String url, String requestJsonData, String requestType) {

        disableConnectionReuseIfNecessary();
        HttpsURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpsURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            urlConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            // handle POST parameters
            if (requestJsonData != null) {

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod(requestType);
                urlConnection.setFixedLengthStreamingMode(requestJsonData.getBytes().length);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
                urlConnection.setRequestProperty("Accept-Charset", "utf-8");

                final OutputStream outputStream = urlConnection.getOutputStream();
                final DataOutputStream dataOutPutStream = new DataOutputStream(outputStream);
                try {
                    dataOutPutStream.write(requestJsonData.getBytes("utf-8"));
                    dataOutPutStream.flush();
                } finally {
                    dataOutPutStream.close();
                    outputStream.close();
                }
                return getResponse(urlConnection);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            closeConnection(urlConnection);
            e.printStackTrace();
            return getResponse(urlConnection != null ? urlConnection.getErrorStream() : null);
        } finally {
            closeConnection(urlConnection);
        }

        return null;
    }

    /**
     * Create a connection from a Request and return a get response.
     *
     * @param url                      URL
     * @param base64EncodedCredentials Credentials in Base64 format
     * @return Response
     */

    public String requestGetUrl(String url, String base64EncodedCredentials, String get) {
        HttpsURLConnection urlConnection = null;
        try {
            URL obj = new URL(url);
            urlConnection = (HttpsURLConnection) obj.openConnection();
            // optional default is GET
            urlConnection.setRequestMethod(Constants.GET);
            urlConnection.setDoOutput(false);
            // add request header
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            urlConnection.setRequestProperty("Accept-Charset", "utf-8");
            urlConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
            //int responseCode = urlConnection.getResponseCode();
            return getResponse(urlConnection);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            closeConnection(urlConnection);
            e.printStackTrace();
            return getResponse(urlConnection != null ? urlConnection.getErrorStream() : null);
        } finally {
            closeConnection(urlConnection);
        }
        return null;

    }

    private String getResponse(HttpsURLConnection urlConnection)
            throws IOException {
        try {
            InputStream inputStream = urlConnection.getInputStream();
            try {
                return getResponse(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            InputStream inputStream = urlConnection.getErrorStream();
            try {
                return getResponse(inputStream);
            } finally {
                inputStream.close();
                closeConnection(urlConnection);
            }
        }
    }

    private void closeConnection(HttpsURLConnection httpUrlConn) {
        if (httpUrlConn != null) {
            httpUrlConn.disconnect();
        }
    }

    private void disableConnectionReuseIfNecessary() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO)
            System.setProperty("http.keepAlive", "false");
    }

    private String getResponse(InputStream inputStream) {
        BufferedReader bufferreader = null;
        StringBuilder sbResponse = new StringBuilder();

        String readLine = null;
        try {
            bufferreader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            readLine = bufferreader.readLine();
            while (readLine != null) {
                sbResponse.append(readLine);
                readLine = bufferreader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferreader != null) {
                try {
                    bufferreader.close();
                } catch (IOException ioExp) {

                    Log.e("getResponse", ioExp.toString());
                }
            }
        }

        return sbResponse.toString();
    }

    /**
     * This method is called when parse json data
     *
     * @param responseString Response String
     * @param responseParams Response Parameters
     * @return Map Object
     */
    @SuppressWarnings("rawtypes")
    public Map readAndParseJSON(String responseString,
                                ArrayList<String> responseParams) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject reader = new JSONObject(responseString);
            for (String name : responseParams) {
                map.put(name, reader.opt(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * This method is called when parse json data
     *
     * @param responseString Response String
     * @param responseParams Response Parameters
     * @return Map Object
     */
    @SuppressWarnings("rawtypes")
    public Map readAndParseJSON1(String responseString,
                                 ArrayList<String> responseParams) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject reader = new JSONObject(responseString);
            for (String name : responseParams) {
                map.put(name, reader.optString(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
