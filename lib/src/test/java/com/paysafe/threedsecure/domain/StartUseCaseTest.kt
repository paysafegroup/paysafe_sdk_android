/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.cm.models.CardinalError
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.Card
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.JwtResponse
import com.paysafe.util.Result
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.util.toJson
import com.paysafe.whenEver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class StartUseCaseTest {

    private val api = mock<NetbanxApi>()
    private val cardinal = mock<Cardinal>()

    private val tested = StartUseCase(api, cardinal)

    @Test
    fun `startUseCase returns deviceFingerprintId`() {
        // given
        val cardBin = "4111111"

        val deviceFingerprint = "deviceFingerprint"
        val jwtToken = "jwtToken"
        val jwtResponse = JwtResponse(
            "123",
            deviceFingerprint,
            jwtToken,
            Card(cardBin)
        )
        val eventType = EventType.SUCCESS
        val logSuccessMessage = "DeviceFingerPrinting completed successfully"

        whenEver(api.jwt(safeEq(cardBin), safeAny())).thenAnswer {
            with(it.arguments[1] as (Result<JwtResponse, ThreeDSecureError>) -> Unit) { this(Result.Success(jwtResponse)) }
        }

        whenEver(cardinal.init(safeEq(jwtToken), safeAny())).thenAnswer {
            with(it.arguments[1] as CardinalInitService) { onSetupCompleted(deviceFingerprint) }
        }

        val mockedCallback = mock<(Result<String, ThreeDSecureError>) -> Unit>()

        // when
        tested(cardBin, mockedCallback)

        // then
        inOrder(api, cardinal, mockedCallback).apply {
            verify(api).jwt(safeEq(cardBin), safeAny())
            verify(cardinal).init(safeEq(jwtToken), safeAny())
            verify(api).log(safeEq(eventType), safeEq(logSuccessMessage))
            verify(mockedCallback).invoke(safeEq(Result.Success(deviceFingerprint)))
        }
    }

    @Test
    fun `startUseCase returns error when JWT Token creation fails`() {
        // given
        val cardBin = "4111111"

        val error =
            ThreeDSecureError(code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR, detailedMessage = "message")

        whenEver(api.jwt(safeEq(cardBin), safeAny())).thenAnswer {
            with(it.arguments[1] as (Result<JwtResponse, ThreeDSecureError>) -> Unit) { this(Result.Failure(error)) }
        }

        val mockedCallback = mock<(Result<String, ThreeDSecureError>) -> Unit>()

        // when
        tested(cardBin, mockedCallback)

        // then
        inOrder(api, mockedCallback).apply {
            verify(api).jwt(safeEq(cardBin), safeAny())
            verify(api).log(safeEq(EventType.INTERNAL_SDK_ERROR), safeEq("/jwt call failed"))
            verify(mockedCallback).invoke(safeEq(Result.Failure(error)))
        }
    }

    @Test
    fun `startUseCase returns error when Cardinal initialization fails`() {
        // given
        val cardBin = "4111111"

        val deviceFingerprint = "deviceFingerprint"
        val jwtToken = "jwtToken"
        val jwtResponse = JwtResponse(
            "123",
            deviceFingerprint,
            jwtToken,
            Card(cardBin)
        )
        val eventType = EventType.INTERNAL_SDK_ERROR

        whenEver(api.jwt(safeEq(cardBin), safeAny())).thenAnswer {
            with(it.arguments[1] as (Result<JwtResponse, ThreeDSecureError>) -> Unit) { this(Result.Success(jwtResponse)) }
        }

        val errorNumber = 13
        val errorDescription = "Expected description"
        val validateResponse = ValidateResponse(CardinalActionCode.FAILURE, CardinalError(errorNumber, errorDescription))
        val expectedError =
            ThreeDSecureError(
                code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                detailedMessage = "The flow could not be started because of an internal exception $errorNumber : $errorDescription."
            )

        whenEver(cardinal.init(safeEq(jwtToken), safeAny())).thenAnswer {
            with(it.arguments[1] as CardinalInitService) { onValidated(validateResponse, null) }
        }

        val mockedCallback = mock<(Result<String, ThreeDSecureError>) -> Unit>()

        // when
        tested(cardBin, mockedCallback)

        // then
        inOrder(api, cardinal, mockedCallback).apply {
            verify(api).jwt(safeEq(cardBin), safeAny())
            verify(cardinal).init(safeEq(jwtToken), safeAny())
            verify(api).log(safeEq(eventType), safeEq("DeviceFingerPrinting failed with: ${validateResponse.toJson()}"))
            verify(mockedCallback).invoke(safeEq(Result.Failure(expectedError)))
        }
    }

    @Test
    fun `startUseCase does nothing when Cardinal initialization fails and validateResponse is null`() {
        // given
        val cardBin = "4111111"

        val deviceFingerprint = "deviceFingerprint"
        val jwtToken = "jwtToken"
        val jwtResponse = JwtResponse(
            "123",
            deviceFingerprint,
            jwtToken,
            Card(cardBin)
        )
        val eventType = EventType.INTERNAL_SDK_ERROR

        whenEver(api.jwt(safeEq(cardBin), safeAny())).thenAnswer {
            with(it.arguments[1] as (Result<JwtResponse, ThreeDSecureError>) -> Unit) { this(Result.Success(jwtResponse)) }
        }

        whenEver(cardinal.init(safeEq(jwtToken), safeAny())).thenAnswer {
            with(it.arguments[1] as CardinalInitService) { onValidated(null, null) }
        }

        val mockedCallback = mock<(Result<String, ThreeDSecureError>) -> Unit>()

        // when
        tested(cardBin, mockedCallback)

        // then
        inOrder(api, cardinal, mockedCallback).apply {
            verify(api).jwt(safeEq(cardBin), safeAny())
            verify(cardinal).init(safeEq(jwtToken), safeAny())
            verify(api, never()).log(safeAny(), safeAny())
            verify(mockedCallback, never()).invoke(safeAny())
        }
    }
}