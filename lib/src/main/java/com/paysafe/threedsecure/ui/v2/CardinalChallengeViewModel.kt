/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui.v2

import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalChallengeObserver
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.domain.HandleChallengeUseCase
import com.paysafe.threedsecure.domain.HandleSuccessfulChallengeUseCase
import com.paysafe.threedsecure.ui.BaseViewModel
import com.paysafe.util.Result

internal class CardinalChallengeViewModel(
    private val handleChallengeUseCase: HandleChallengeUseCase,
    private val handleSuccessfulChallengeUseCase: HandleSuccessfulChallengeUseCase,
    finalizeUseCase: FinalizeUseCase
) : BaseViewModel(finalizeUseCase) {

    fun handleChallenge(
        cardinalChallengeObserver: CardinalChallengeObserver,
        challengePayload: ChallengePayload,
        cardinal: Cardinal
    ) {
        handleChallengeUseCase(cardinalChallengeObserver, challengePayload, cardinal)
    }

    fun onChallengePassed(
        challengePayload: ChallengePayload,
        validateResponse: ValidateResponse,
        serverJwt: String?
    ) {
        handleSuccessfulChallengeUseCase(challengePayload, validateResponse, serverJwt) {
            with(it as Result.Success) { finalize(data) }
        }
    }
}
