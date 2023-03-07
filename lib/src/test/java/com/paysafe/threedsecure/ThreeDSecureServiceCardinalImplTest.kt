/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.domain.ProcessPayloadUseCase
import com.paysafe.threedsecure.domain.StartUseCase
import com.paysafe.util.Result
import com.paysafe.util.Scheduler
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ThreeDSecureServiceCardinalImplTest {

    private val startUseCase = mock<StartUseCase>()
    private val processPayloadUseCase = mock<ProcessPayloadUseCase>()
    private val handlerCallback = object : Scheduler {
        override fun post(r: () -> Unit) {
            r();
        }
    }

    private val tested =
        ThreeDSecureServiceCardinalImpl(
            startUseCase,
            processPayloadUseCase,
            handlerCallback
        )

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `start calls onError() when token creation fails`() {
        // given
        val cardBin = "41111111"

        val error =
            ThreeDSecureError(
                code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                detailedMessage = "message"
            )

        `when`(
            startUseCase.invoke(
                safeEq(cardBin),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<String, ThreeDSecureError>) -> Unit) {
                this(
                    Result.Failure(
                        error
                    )
                )
            }
        }

        val mockCallback = mock(ThreeDSStartCallback::class.java)

        // when
        tested.start(cardBin, mockCallback)

        // then
        verify(mockCallback).onError(safeEq(error))
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `start calls onSuccess() when device fingerprint created`() {
        // given
        val cardBin = "41111111"

        val deviceFingerprint = "deviceFingerprint"

        `when`(
            startUseCase.invoke(
                safeEq(cardBin),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<String, ThreeDSecureError>) -> Unit) {
                this(
                    Result.Success(
                        deviceFingerprint
                    )
                )
            }
        }

        val mockCallback = mock(ThreeDSStartCallback::class.java)

        tested.start(cardBin, mockCallback)

        // then
        verify(mockCallback).onSuccess(safeEq(deviceFingerprint))
    }

}