package com.paysafe.threedsecure.domain

import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.paysafe.Mockable
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.util.toJson
import com.paysafe.util.Result

@Mockable
internal class HandleSuccessfulChallengeUseCase(private val api: NetbanxApi) {

    internal operator fun invoke(
        challengePayload: ChallengePayload,
        validateResponse: ValidateResponse,
        serverJwt: String?,
        callback: (Result<ChallengeData, ThreeDSecureError>) -> Unit
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
