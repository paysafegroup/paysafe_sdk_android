/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.paysafe.mock
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.util.Result
import com.paysafe.util.base64Encode
import com.paysafe.util.createChallenge
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.verify

@RunWith(JUnit4::class)
class ProcessPayloadUseCaseTest {

    private val tested = ProcessPayloadUseCase()

    @Test
    fun `processPayloadUseCase returns ChallengePayload`() {
        // given
        val challenge = createChallenge()

        val payload = JSONObject()
            .put("id", challenge.authId)
            .put("accountId", challenge.accountId)
            .put("transactionId", challenge.transactionId)
            .put("payload", challenge.payload)
            .put("threeDSecureVersion", challenge.threeDSecureVersion)
            .toString()
            .base64Encode()

        val mockCallback = mock<(Result<ChallengePayload, ThreeDSecureError>) -> Unit>()

        // when
        tested.invoke(payload, mockCallback)

        // then
        verify(mockCallback).invoke(safeEq(Result.Success(challenge)))
    }

    @Test
    fun `processPayloadUseCase returns error`() {
        // given
        val payload = "invalid".base64Encode()

        val error = ThreeDSecureError(
            code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
            detailedMessage = "Invalid SDK challenge payload: $payload"
        )

        val mockCallback = mock<(Result<ChallengePayload, ThreeDSecureError>) -> Unit>()

        // when
        tested.invoke(payload, mockCallback)

        // then
        verify(mockCallback).invoke(safeEq(Result.Failure(error)))
    }

}