/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui.v1

import com.paysafe.R
import com.paysafe.threedsecure.ui.BaseViewModel
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.parseThreeDSecureError
import com.paysafe.threedsecure.util.Event
import org.json.JSONException

internal class WebViewChallengeViewModel(finalizeUseCase: FinalizeUseCase) : BaseViewModel(finalizeUseCase) {

    internal fun onChallengeValidated(authenticationId: String, errorJsonPayload: String) {
        _result.value = try {
            with(
                errorJsonPayload.parseThreeDSecureError()?.let { ChallengeResult.Failure(it) }
                    ?: ChallengeResult.Success(authenticationId)
            ) {
                Event(this)
            }
        } catch (e: JSONException) {
            Event(
                ChallengeResult.Failure(
                    ThreeDSecureError(
                        code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
                        detailedMessage = "Could not parse error payload: $errorJsonPayload"
                    )
                )
            )
        }
    }

    internal fun onMenuItemSelected(itemId: Int, challengePayload: ChallengePayload) =
        when (itemId) {
            R.id.action_cancel -> {
                finalize(ChallengeData(null, challengePayload.accountId, challengePayload.authId, null, FinalizeStatus.CANCELLED))
                true
            }
            else -> false
        }

}