/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalChallengeObserver
import com.paysafe.Mockable
import com.paysafe.threedsecure.data.ChallengePayload

@Mockable
internal class HandleChallengeUseCase {

    internal operator fun invoke(
        cardinalChallengeObserver: CardinalChallengeObserver,
        challengePayload: ChallengePayload,
        cardinal: Cardinal
    ) {
        cardinal.cca_continue(
            challengePayload.transactionId,
            challengePayload.payload,
            cardinalChallengeObserver
        )
    }
}
