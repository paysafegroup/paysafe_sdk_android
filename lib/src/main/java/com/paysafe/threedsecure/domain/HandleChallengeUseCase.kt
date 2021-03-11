/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import android.app.Activity
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.paysafe.Mockable
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.*
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.util.Result
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.util.toJson

@Mockable
internal class HandleChallengeUseCase(private val api: NetbanxApi, private val cardinal: Cardinal) {

    internal operator fun invoke(
        activity: Activity,
        challengePayload: ChallengePayload,
        callback: (Result<ChallengeData, ThreeDSecureError>) -> Unit
    ) {
        cardinal.cca_continue(
            challengePayload.transactionId,
            challengePayload.payload,
            activity
        ) { _, validateResponse, serverJwt ->

            onChallengePassed(challengePayload, validateResponse, callback, serverJwt)
        }
    }

    private fun onChallengePassed(
        challengePayload: ChallengePayload,
        validateResponse: ValidateResponse,
        callback: (Result<ChallengeData, ThreeDSecureError>) -> Unit,
        serverJwt: String?
    ) {
        api.log(
            EventType.SUCCESS,
            "Challenge for authentication: ${challengePayload.authId} passed"
        )
        val finalizeStatus =
            if (validateResponse.actionCode == CardinalActionCode.ERROR) FinalizeStatus.FAILED
            else FinalizeStatus.SUCCESSFUL
        callback(
            Result.Success(
                ChallengeData(
                    validateResponse.toJson(),
                    challengePayload.accountId,
                    challengePayload.authId,
                    serverJwt,
                    finalizeStatus
                )
            )
        )
    }

}