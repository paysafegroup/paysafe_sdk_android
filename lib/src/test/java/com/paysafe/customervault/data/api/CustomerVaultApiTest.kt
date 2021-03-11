/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault.data.api

import com.paysafe.*
import com.paysafe.common.Error
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.customervault.newGooglePaySingleUseToken
import com.paysafe.customervault.newGooglePayTokenParams
import com.paysafe.customervault.newSingleUseToken
import com.paysafe.customervault.newSingleUseTokenParams
import com.paysafe.util.Result
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class CustomerVaultApiTest {

    private val client = mock<PaysafeApiClient>()

    private val tested = CustomerVaultApi(client)

    @Test
    fun `createSingleUseToken() returns success`() {
        //given
        val params = newSingleUseTokenParams()

        val expectedTokenRequest = newSingleUseTokenRequest(params)

        val singleUseToken = newSingleUseToken()
        whenEver(
            client.execute(
                safeEq(SingleUseToken::class.java),
                safeEq(expectedTokenRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<SingleUseToken>) -> Unit) {
                this(ApiResponse.Success(singleUseToken))
            }
        }

        val mockCallback = mock<(Result<SingleUseToken, Error>) -> Unit>()

        // when
        tested.createSingleUseToken(params, mockCallback)

        // then
        inOrder(client, mockCallback).apply {
            verify(client).execute(
                safeEq(SingleUseToken::class.java),
                safeEq(expectedTokenRequest),
                safeAny()
            )
            verify(mockCallback).invoke(safeEq(Result.Success(singleUseToken)))
        }
    }

    @Test
    fun `createSingleUseToken() returns failure on connection failed`() {
        `createSingleUseToken() returns failure with error`(
            errorCode = "9001",
            errorMessage = "The HTTP request could not be executed due to connectivity issues, cancellation or a timeout"
        ) {
            ApiResponse.Failure.ConnectionFailed
        }
    }

    @Test
    fun `createSingleUseToken() returns failure on invalid merchant configuration`() {
        `createSingleUseToken() returns failure with error` {
            ApiResponse.Failure.InvalidMerchantConfiguration(
                it
            )
        }
    }

    @Test
    fun `createSingleUseToken() returns failure on invalid API key`() {
        `createSingleUseToken() returns failure with error` { ApiResponse.Failure.InvalidApiKey(it) }
    }

    @Test
    fun `createSingleUseToken() returns failure on internal SDK error`() {
        `createSingleUseToken() returns failure with error` {
            ApiResponse.Failure.InternalSdkError(
                it
            )
        }
    }

    private fun `createSingleUseToken() returns failure with error`(
        errorCode: String = "123",
        errorMessage: String = "Some error message",
        failureFactory: (Error) -> ApiResponse.Failure
    ) {
        // given
        val params = newSingleUseTokenParams()

        val expectedTokenRequest = newSingleUseTokenRequest(params)

        val expectedError = Error(
            code = errorCode,
            message = errorMessage
        )

        whenEver(
            client.execute(
                safeEq(SingleUseToken::class.java),
                safeEq(expectedTokenRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<SingleUseToken>) -> Unit) {
                this(failureFactory(expectedError))
            }
        }


        val mockCallback = mock<(Result<SingleUseToken, Error>) -> Unit>()

        // when
        tested.createSingleUseToken(params, mockCallback)

        // then
        inOrder(client, mockCallback).apply {
            verify(client).execute(
                safeEq(SingleUseToken::class.java),
                safeEq(expectedTokenRequest),
                safeAny()
            )
            verify(mockCallback).invoke(safeEq(Result.Failure(expectedError)))
        }

    }

    private fun newSingleUseTokenRequest(params: SingleUseTokenParams): ApiRequest {
        return ApiRequest(
            path = "customervault/v1/singleusetokens",
            body = params
        )
    }

    @Test
    fun `createGooglePaySingleUseToken() returns success`() {
        val params = newGooglePayTokenParams()

        val expectedRequest = newGoogleSingleUseTokenRequest(params)

        val googlePaySingleUseToken = newGooglePaySingleUseToken()

        whenEver(
            client.execute(
                safeEq(GooglePaySingleUseToken::class.java),
                safeEq(expectedRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<GooglePaySingleUseToken>) -> Unit) {
                this(ApiResponse.Success(googlePaySingleUseToken))
            }
        }

        val mockCallback = mock<(Result<GooglePaySingleUseToken, Error>) -> Unit>()

        // when
        tested.createGooglePaySingleUseToken(params, mockCallback)

        // then
        inOrder(client, mockCallback).apply {
            verify(client).execute(
                safeEq(GooglePaySingleUseToken::class.java),
                safeEq(expectedRequest),
                safeAny()
            )
            verify(mockCallback).invoke(safeEq(Result.Success(googlePaySingleUseToken)))
        }
    }

    private fun newGoogleSingleUseTokenRequest(params: GooglePayTokenParams): ApiRequest {
        return ApiRequest(
            path = "customervault/v1/googlepaysingleusetokens",
            body = params
        )
    }

    @Test
    fun `createGooglePaySingleUseToken() returns failure on connection failed`() {
        `createGooglePaySingleUseToken() returns failure with error`(
            errorCode = "9001",
            errorMessage = "The HTTP request could not be executed due to connectivity issues, cancellation or a timeout"
        ) {
            ApiResponse.Failure.ConnectionFailed
        }
    }

    @Test
    fun `createGooglePaySingleUseToken() returns failure on invalid merchant configuration`() {
        `createGooglePaySingleUseToken() returns failure with error` {
            ApiResponse.Failure.InvalidMerchantConfiguration(
                it
            )
        }
    }

    @Test
    fun `createGooglePaySingleUseToken() returns failure on invalid API key`() {
        `createGooglePaySingleUseToken() returns failure with error` {
            ApiResponse.Failure.InvalidMerchantConfiguration(
                it
            )
        }
    }

    @Test
    fun `createGooglePaySingleUseToken() returns failure on internal SDK error`() {
        `createGooglePaySingleUseToken() returns failure with error` {
            ApiResponse.Failure.InvalidMerchantConfiguration(
                it
            )
        }
    }


    private fun `createGooglePaySingleUseToken() returns failure with error`(
        errorCode: String = "123",
        errorMessage: String = "Some error message",
        failureFactory: (Error) -> ApiResponse.Failure
    ) {
        // given
        val params = newGooglePayTokenParams()

        val expectedRequest = newGoogleSingleUseTokenRequest(params)

        val expectedError = Error(
            code = errorCode,
            message = errorMessage
        )

        whenEver(
            client.execute(
                safeEq(GooglePaySingleUseToken::class.java),
                safeEq(expectedRequest),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[2] as (ApiResponse<SingleUseToken>) -> Unit) {
                this(failureFactory(expectedError))
            }
        }


        val mockCallback = mock<(Result<GooglePaySingleUseToken, Error>) -> Unit>()

        // when
        tested.createGooglePaySingleUseToken(params, mockCallback)

        // then
        inOrder(client, mockCallback).apply {
            verify(client).execute(
                safeEq(GooglePaySingleUseToken::class.java),
                safeEq(expectedRequest),
                safeAny()
            )
            verify(mockCallback).invoke(safeEq(Result.Failure(expectedError)))
        }

    }
}