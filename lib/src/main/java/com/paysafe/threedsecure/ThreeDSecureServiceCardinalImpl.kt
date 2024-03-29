/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import android.content.Context
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.domain.ProcessPayloadUseCase
import com.paysafe.threedsecure.domain.StartUseCase
import com.paysafe.threedsecure.ui.v2.CardinalChallengeActivity
import com.paysafe.util.MainThreadScheduler
import com.paysafe.util.Result
import com.paysafe.util.Scheduler

internal class ThreeDSecureServiceCardinalImpl(
    private val startUseCase: StartUseCase,
    private val processPayloadUseCase: ProcessPayloadUseCase,
    private val callbackScheduler: Scheduler = MainThreadScheduler()
) : ThreeDSecureService {

    override fun start(cardBin: String, callback: ThreeDSStartCallback) {
        startUseCase(cardBin) { result ->
            when (result) {
                is Result.Success -> callbackScheduler.post { callback.onSuccess(result.data) }
                is Result.Failure -> callbackScheduler.post { callback.onError(result.error) }
            }
        }
    }

    override fun challenge(context: Context, payload: String, callback: ThreeDSChallengeCallback) {
        processPayloadUseCase(payload) {
            when (it) {
                is Result.Success ->
                        callbackScheduler.post { callback.onSuccess(threeDs2ChallengeResolution(context, it)) }

                is Result.Failure -> callbackScheduler.post {
                    callback.onError(
                        ThreeDSecureError(
                            code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                            detailedMessage = "Invalid SDK challenge payload: $payload"
                        )
                    )
                }
            }
        }
    }

    private fun threeDs2ChallengeResolution(
        context: Context,
        it: Result.Success<ChallengePayload>
    ) = ChallengeResolution(
        CardinalChallengeActivity.createStartIntent(
            context.applicationContext,
            it.data
        )
    )

    companion object {

        private const val THREE_DS_VERSION_2_PREFIX = "2."

    }
}




