/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.paysafe.Mockable
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.util.Result

@Mockable
internal class FinalizeUseCase(private val api: NetbanxApi) {

    internal operator fun invoke(
        challengeData: ChallengeData,
        callback: (Result<Unit, ThreeDSecureError>) -> Unit
    ) {
        val serverJwt =
            if (challengeData.serverJwt.isNullOrBlank()) null else challengeData.serverJwt
        api.finalize(challengeData.accountId, challengeData.authenticationId, serverJwt) {
            if (challengeData.finalizeStatus == FinalizeStatus.FAILED) {
                api.log(
                    EventType.INTERNAL_SDK_ERROR,
                    "Error occurred during challenge for authentication: " +
                            "${challengeData.authenticationId} failed with " +
                            "${challengeData.validateResponse}"
                )
            }

            if (it is Result.Failure) {
                api.log(EventType.INTERNAL_SDK_ERROR, "/finalize call failed")
            }

            callback(it)
        }
    }

}