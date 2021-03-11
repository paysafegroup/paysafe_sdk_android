/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data.api

import android.net.Uri
import com.paysafe.*
import com.paysafe.common.Error
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.*
import com.paysafe.util.Result
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class NetbanxApiTest {

    private val client = mock<PaysafeApiClient>()

    private val generateGUID = mock<() -> String>()

    private val tested = NetbanxApi(client, generateGUID)

    @Before
    fun setUp() {
        whenEver(
            generateGUID()
        ).thenReturn("correlationId")

        tested.correlationId = "correlationId"
    }

    @Test
    fun `jwt() returns success`() {
        // given
        val accountId = "1234"
        whenEver(client.account).thenReturn(accountId)

        val cardBin = "41111111"
        val jwtRequest = createJwtRequest(accountId, cardBin)
        val jwtToken = createJwtToken(cardBin)

        whenEver(
            client.execute(
                safeEq(JwtResponse::class.java),
                safeEq(jwtRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<JwtResponse>) -> Unit) {
                this(
                    ApiResponse.Success(
                        jwtToken,
                        mapOf(NetbanxApi.HEADER_CORRELATION_ID to "correlationId")
                    )
                )
            }
        }

        val mockedCallback = mock<(Result<JwtResponse, ThreeDSecureError>) -> Unit>()

        // when
        tested.jwt(cardBin, mockedCallback)

        // then
        inOrder(client, mockedCallback).apply {
            verify(client).execute(safeEq(JwtResponse::class.java), safeEq(jwtRequest), safeAny())
            verify(mockedCallback).invoke(safeEq(Result.Success(jwtToken)))
        }
    }

    @Test
    fun `jwt() returns failure on connection failed`() {
        `jwt() returns failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_CONNECTION_FAILED,
            detailedMessage = "The HTTP request could not be executed due to connectivity issues, cancellation or a timeout",
            correlationId = null
        ) { _, _ -> ApiResponse.Failure.ConnectionFailed }
    }

    @Test
    fun `jwt() returns failure on invalid merchant configuration`() {
        `jwt() returns failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_INVALID_MERCHANT_CONFIGURATION,
            detailedMessage = "Invalid merchant configuration setup"
        ) { error, correlationId ->
            ApiResponse.Failure.InvalidMerchantConfiguration(
                error,
                correlationId?.let { mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId) }
                    ?: emptyMap()
            )
        }
    }

    @Test
    fun `jwt() returns failure on invalid API key`() {
        `jwt() returns failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_INVALID_API_KEY,
            detailedMessage = "Invalid apiKey or apiSecret"
        ) { error, correlationId ->
            ApiResponse.Failure.InvalidApiKey(
                error,
                correlationId?.let { mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId) }
                    ?: emptyMap()
            )
        }
    }

    @Test
    fun `jwt() returns failure on internal SDK error`() {
        `jwt() returns failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
            detailedMessage = "An unexpected error occurred"
        ) { error, correlationId ->
            ApiResponse.Failure.InternalSdkError(
                error,
                correlationId?.let { mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId) }
                    ?: emptyMap()
            )
        }
    }

    private fun `jwt() returns failure with details`(
        errorCode: String,
        detailedMessage: String,
        correlationId: String? = "correlationId",
        failureFactory: (Error, String?) -> ApiResponse.Failure
    ) {
        // given
        val accountId = "1234"
        whenEver(client.account).thenReturn(accountId)

        val cardBin = "41111111"

        val jwtRequest = createJwtRequest(accountId, cardBin)

        val apiError = createError()

        val expectedError = ThreeDSecureError(
            code = errorCode,
            detailedMessage = detailedMessage,
            correlationId = correlationId
        )

        whenEver(
            client.execute(
                safeEq(JwtResponse::class.java),
                safeEq(jwtRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<JwtResponse>) -> Unit) {
                this(
                    failureFactory(apiError, correlationId)
                )
            }
        }

        val mockedCallback = mock<(Result<JwtResponse, ThreeDSecureError>) -> Unit>()

        // when
        tested.jwt(cardBin, mockedCallback)

        // then
        inOrder(client, mockedCallback).apply {
            verify(client).execute(safeEq(JwtResponse::class.java), safeEq(jwtRequest), safeAny())
            verify(mockedCallback).invoke(safeEq(Result.Failure(expectedError)))
        }
    }

    private fun createJwtRequest(accountId: String, cardBin: String): ApiRequest =
        ApiRequest(
            "threedsecure/v2/jwt",
            JwtRequest(accountId, Card(cardBin)),
            mapOf(NetbanxApi.HEADER_CORRELATION_ID to "correlationId")
        )

    private fun createJwtToken(cardBin: String): JwtResponse =
        JwtResponse(
            "1234",
            "testFingerprint",
            "testJwt",
            Card(cardBin)
        )

    @Test
    fun `finalize() returns success`() {
        // given
        val accountId = "accountId"
        val authenticationId = "authenticationId"
        val serverJwt = "serverJwt"

        val finalizeRequest = createFinalizeRequest(accountId, authenticationId, serverJwt)

        whenEver(
            client.execute(
                safeEq(Unit::class.java),
                safeEq(finalizeRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<Unit>) -> Unit) {
                this(
                    ApiResponse.Success(
                        Unit,
                        mapOf(NetbanxApi.HEADER_CORRELATION_ID to "correlationId")
                    )
                )
            }
        }

        val mockedCallback = mock<(Result<Unit, ThreeDSecureError>) -> Unit>()

        // when
        tested.finalize(accountId, authenticationId, serverJwt, mockedCallback)

        // then
        inOrder(client, mockedCallback).apply {
            verify(client).execute(safeEq(Unit::class.java), safeEq(finalizeRequest), safeAny())
            verify(mockedCallback).invoke(safeEq(Result.Success(Unit)))
        }
    }

    @Test
    fun `finalize() returns failure on connection failed`() {
        `finalize() return failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_CONNECTION_FAILED,
            detailedMessage = "The HTTP request could not be executed due to connectivity issues, cancellation or a timeout",
            correlationId = null
        ) { _, _ -> ApiResponse.Failure.ConnectionFailed }
    }

    @Test
    fun `finalize() returns failure on invalid merchant configuration`() {
        `finalize() return failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_INVALID_MERCHANT_CONFIGURATION,
            detailedMessage = "Invalid merchant configuration setup"
        ) { apiError, correlationId ->
            ApiResponse.Failure.InvalidMerchantConfiguration(
                apiError,
                correlationId?.let { mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId) }
                    ?: emptyMap()
            )
        }
    }

    @Test
    fun `finalize() returns failure on invalid API key`() {
        `finalize() return failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_INVALID_API_KEY,
            detailedMessage = "Invalid apiKey or apiSecret"
        ) { apiError, correlationId ->
            ApiResponse.Failure.InvalidApiKey(
                apiError,
                correlationId?.let { mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId) }
                    ?: emptyMap()
            )
        }
    }

    @Test
    fun `finalize() returns failure on internal SDK error`() {
        `finalize() return failure with details`(
            errorCode = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
            detailedMessage = "An unexpected error occurred"
        ) { apiError, correlationId ->
            ApiResponse.Failure.InternalSdkError(
                apiError,
                correlationId?.let { mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId) }
                    ?: emptyMap()
            )
        }
    }

    private fun `finalize() return failure with details`(
        errorCode: String,
        detailedMessage: String,
        correlationId: String? = "correlationId",
        failureFactory: (Error, String?) -> ApiResponse.Failure
    ) {
        // given
        val accountId = "accountId"
        val authenticationId = "authenticationId"
        val serverJwt = "serverJwt"

        val finalizeRequest = createFinalizeRequest(accountId, authenticationId, serverJwt)

        val apiError = createError()

        val expectedError = ThreeDSecureError(
            code = errorCode,
            detailedMessage = detailedMessage,
            correlationId = correlationId
        )

        whenEver(
            client.execute(
                safeEq(Unit::class.java),
                safeEq(finalizeRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<Unit>) -> Unit) {
                this(
                    failureFactory(
                        apiError,
                        correlationId
                    )
                )
            }
        }

        val mockedCallback = mock<(Result<Unit, ThreeDSecureError>) -> Unit>()

        // when
        tested.finalize("accountId", "authenticationId", "serverJwt", mockedCallback)

        // then
        inOrder(client, mockedCallback).apply {
            verify(client).execute(safeEq(Unit::class.java), safeEq(finalizeRequest), safeAny())
            verify(mockedCallback).invoke(safeEq(Result.Failure(expectedError)))
        }
    }

    private fun createError(): Error =
        Error(
            code = "1234",
            message = "Error message"
        )

    private fun createFinalizeRequest(
        accountId: String,
        authenticationId: String,
        serverJwt: String
    ) =
        ApiRequest(
            path = Uri.Builder()
                .appendPath("threedsecure")
                .appendPath("v2")
                .appendPath("accounts")
                .appendPath(accountId)
                .appendPath("authentications")
                .appendPath(authenticationId)
                .appendPath("finalize")
                .build()
                .toString(),
            body = FinalizeRequest(serverJwt),
            headers = mapOf(NetbanxApi.HEADER_CORRELATION_ID to "correlationId")
        )


    @Test
    fun `log() returns success`() {
        // given
        val eventType = EventType.SUCCESS
        val message = "Successful log"

        val logRequest = createLogRequest(eventType, message)

        val correlationId = "correlationId"
        whenEver(
            client.execute(
                safeEq(Unit::class.java),
                safeEq(logRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<Unit>) -> Unit) {
                this(
                    ApiResponse.Success(
                        Unit,
                        mapOf(NetbanxApi.HEADER_CORRELATION_ID to correlationId)
                    )
                )
            }
        }

        // when
        tested.log(eventType, message)

        // then
        inOrder(client).apply {
            verify(client).execute(safeEq(Unit::class.java), safeEq(logRequest), safeAny())
        }
    }

    @Test
    fun `log() returns error`() {
        // given
        val eventType = EventType.INTERNAL_SDK_ERROR
        val message = "Successful log"

        val logRequest = createLogRequest(eventType, message)

        val apiError = createError()

        whenEver(
            client.execute(
                safeEq(Unit::class.java),
                safeEq(logRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<Unit>) -> Unit) {
                this(
                    ApiResponse.Failure.InternalSdkError(
                        apiError,
                        mapOf(NetbanxApi.HEADER_CORRELATION_ID to "correlationId")
                    )
                )
            }
        }

        // when
        tested.log(eventType, message)

        // then
        inOrder(client).apply {
            verify(client).execute(safeEq(Unit::class.java), safeEq(logRequest), safeAny())
        }
    }

    private fun createLogRequest(eventType: EventType, message: String) =
        ApiRequest(
            "threedsecure/v2/log",
            LogRequest(eventType, message),
            mapOf(NetbanxApi.HEADER_CORRELATION_ID to "correlationId")
        )

}