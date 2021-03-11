/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.data.authentications.service

import com.paysafe.demo.data.authentications.AuthenticationIdResponse
import com.paysafe.demo.data.authentications.AuthenticationRequest
import com.paysafe.demo.data.authentications.AuthenticationResponse
import com.paysafe.demo.data.authentications.AuthorizeRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MerchantBackendService {

    @POST("/threedsecure/v2/accounts/{merchant_account}/authentications")
    suspend fun getChallengePayload(
        @Header("Authorization") credentials: String,
        @Path("merchant_account") merchantAccountNumber: String,
        @Body body: AuthenticationRequest
    ): Response<AuthenticationResponse>

    @GET("/threedsecure/v2/accounts/{merchant_account}/authentications/{authentication_id}")
    suspend fun authenticationsLookup(
        @Header("Authorization") credentials: String,
        @Path("merchant_account") merchantAccountNumber: String,
        @Path("authentication_id") authenticationId: String
    ): Response<AuthenticationIdResponse>

    @POST("/cardpayments/v1/accounts/{account_id}/auths")
    suspend fun authorize(
        @Header("Authorization") credentials: String,
        @Path("account_id") accountId: String,
        @Body body: AuthorizeRequest
    ): Response<AuthenticationIdResponse>

    companion object {
        private const val BASE_URL = "https://test.api.paysafe.com"

        fun create(): MerchantBackendService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .addInterceptor(HeadersInterceptor())
                        .build()
                )
                .build()
                .create(MerchantBackendService::class.java)
        }
    }

    class HeadersInterceptor : Interceptor {

        /**
         * Interceptor class for setting of the headers for every request
         */
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            return chain.proceed(request)
        }
    }
}