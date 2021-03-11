/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.paysafe.common.ErrorResponse
import com.paysafe.util.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

/**
 * A client that handles HTTP communication with the Paysafe APIs.
 */
@Mockable
class PaysafeApiClient internal constructor(
    internal val keyId: String,
    internal val keyPassword: String,
    internal val account: String,
    internal val environment: Environment,
    connectionTimeout: Long,
    readTimeout: Long,
    isHttpLoggingEnabled: Boolean
) {

    private val baseUrl = HttpUrl.get(environment.url)

    private val httpClient: OkHttpClient = okHttpClient {
        connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
        readTimeout(readTimeout, TimeUnit.MILLISECONDS)

        basicAuth {
            username = keyId
            password = keyPassword
        }

        logger {
            tag = PaysafeApiClient::class.java.simpleName
            level =
                if (isHttpLoggingEnabled) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    private val gson: Gson = gson {}

    private val callbackHandler = MainThreadScheduler()

    internal final inline fun <reified T> execute(
        apiRequest: ApiRequest,
        noinline callback: (ApiResponse<T>) -> Unit
    ) = execute(T::class.java, apiRequest, callback)

    @VisibleForTesting
    internal fun <T> execute(
        responseType: Class<T>,
        apiRequest: ApiRequest,
        callback: (ApiResponse<T>) -> Unit
    ) {
        val request = Request.Builder()
            .url(baseUrl.newBuilder().addPathSegments(apiRequest.path).build())
            .post(RequestBody.create(JSON_MEDIA_TYPE, gson.toJson(apiRequest.body)))
            .headers(Headers.of(apiRequest.headers))
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callbackHandler.post { callback(ApiResponse.Failure.ConnectionFailed) }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val apiResponse: ApiResponse<T> = processResponse(responseType, response)
                    callbackHandler.post { callback(apiResponse) }
                } catch (e: JsonParseException) {
                    callbackHandler.post {
                        callback(
                            ApiResponse.Failure.InternalSdkError(
                                null,
                                response.headers().toMap()
                            )
                        )
                    }
                }
            }

        })
    }

    private fun <T> processResponse(responseType: Class<T>, response: Response): ApiResponse<T> {
        return if (response.isSuccessful) {
            handleHttpResponse(responseType, response)
        } else {
            handleHttpError(response)
        }
    }

    private fun <T> handleHttpResponse(responseType: Class<T>, response: Response): ApiResponse<T> {
        if (Unit::class.java == responseType) {
            return ApiResponse.Success(Unit, response.headers().toMap()) as ApiResponse<T>
        }

        val payload = gson.fromJson(response.body()!!.charStream(), responseType)
        return ApiResponse.Success(payload, response.headers().toMap())
    }


    private fun <T> handleHttpError(response: Response): ApiResponse<T> {
        val errorResponse = gson.fromJson(response.body()!!.charStream(), ErrorResponse::class.java)
        val headers = response.headers().toMap()

        return when {
            errorResponse.error == null -> ApiResponse.Failure.InternalSdkError(null, headers);

            ERROR_CODE_INVALID_MERCHANT_CONFIGURATION == errorResponse.error.code -> ApiResponse.Failure.InvalidMerchantConfiguration(
                errorResponse.error,
                headers
            )

            HttpURLConnection.HTTP_UNAUTHORIZED == response.code()
                    || HttpURLConnection.HTTP_FORBIDDEN == response.code() -> ApiResponse.Failure.InvalidApiKey(
                errorResponse.error,
                headers
            )

            else -> ApiResponse.Failure.InternalSdkError(errorResponse.error, headers)
        }
    }

    companion object {

        private val JSON_MEDIA_TYPE = MediaType.parse("application/json")

        private const val ERROR_CODE_INVALID_MERCHANT_CONFIGURATION = "5050"

    }

    @PaysafeDsl
    class Builder {

        /**
         * The API key used for authentication.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var apiKey: String

        /**
         * The API secret used for authentication.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var apiSecret: String

        /**
         * The Paysafe account ID.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var accountId: String

        /**
         * The target environment. Use [Environment.LIVE] for production and
         * [Environment.TEST] for a testing sandbox.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var environment: Environment

        /**
         * The timeout for new connections. A value of `0` means no timeout,
         * otherwise the value must be between 1 and [Integer.MAX_VALUE] when
         * converted to milliseconds.
         *
         * The default value is 10 seconds.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var connectionTimeout = 10000L

        /**
         * The timeout for requests. A value of `0` means no timeout,
         * otherwise the value must be between 1 and [Integer.MAX_VALUE] when
         * converted to milliseconds.
         *
         * The default value is 10 seconds.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var requestTimeout = 10000L

        /**
         * Indicates if HTTP request and responses should be logged to the console.
         *
         * We recommend to keep this `false` in production.
         *
         * The default value is `false`.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var httpLoggingEnabled = false

        /**
         * Sets apiClient api key
         * Used for better readability when building client in java code
         */
        fun withApiKey(apiKey: String) = also { this.apiKey = apiKey }

        /**
         * Sets apiClient api secret
         * Used for better readability when building client in java code
         */
        fun withApiSecret(apiSecret: String) = also { this.apiSecret = apiSecret }

        /**
         * Sets apiClient account id
         * Used for better readability when building client in java code
         */
        fun withAccountId(accountId: String) = also { this.accountId = accountId }

        /**
         * Sets apiClient environment
         * Used for better readability when building client in java code
         */
        fun withEnvironment(environment: Environment) = also { this.environment = environment }

        /**
         * Sets apiClient connection timeout
         * Used for better readability when building client in java code
         */
        fun withConnectionTimeout(connectionTimeout: Long) =
            also { this.connectionTimeout = connectionTimeout }

        /**
         * Sets apiClient request timeout
         * Used for better readability when building client in java code
         */
        fun withRequestTimeout(requestTimeout: Long) = also { this.requestTimeout = requestTimeout }

        /**
         * Sets apiClient http logging
         * Used for better readability when building client in java code
         */
        fun withHttpLoggingEnabled(httpLoggingEnabled: Boolean) =
            also { this.httpLoggingEnabled = httpLoggingEnabled }

        fun build(): PaysafeApiClient {
            check(::apiKey.isInitialized) { "API Key required, but missing" }
            check(::apiSecret.isInitialized) { "API Secret is required, but missing" }
            check(::accountId.isInitialized) { "Account ID is required, but missing" }
            check(::environment.isInitialized) { "Environment is required, but missing" }

            return PaysafeApiClient(
                apiKey,
                apiSecret,
                accountId,
                environment,
                connectionTimeout,
                requestTimeout,
                httpLoggingEnabled
            )
        }
    }

}
