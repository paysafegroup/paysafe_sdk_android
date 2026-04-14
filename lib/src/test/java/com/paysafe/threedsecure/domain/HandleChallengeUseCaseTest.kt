/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalChallengeObserver
import com.paysafe.mock
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.util.createChallenge
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.verify


@RunWith(JUnit4::class)
class HandleChallengeUseCaseTest {

    private val tested = HandleChallengeUseCase()

    @Test
    fun `handleChallengeUseCase calls cca_continue with correct parameters`() {
        // given
        val cardinalChallengeObserver = mock<CardinalChallengeObserver>()
        val challenge = createChallenge()
        val challengePayload = ChallengePayload(
            authId = challenge.authId,
            accountId = challenge.accountId,
            transactionId = challenge.transactionId,
            payload = "any",
            threeDSecureVersion = "2.0",
        )
        val cardinal = mock<Cardinal>()

        // when
        tested(cardinalChallengeObserver, challengePayload, cardinal)

        // then
        verify(cardinal).cca_continue(
            challenge.transactionId,
            challengePayload.payload,
            cardinalChallengeObserver,
        )
    }
}
