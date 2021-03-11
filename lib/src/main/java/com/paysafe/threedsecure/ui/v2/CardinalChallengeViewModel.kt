/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui.v2

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.util.Result
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.domain.HandleChallengeUseCase
import com.paysafe.threedsecure.ui.BaseViewModel

internal class CardinalChallengeViewModel(
    private val handleChallengeUseCase: HandleChallengeUseCase,
    finalizeUseCase: FinalizeUseCase
) : BaseViewModel(finalizeUseCase) {

    fun onValidateChallenge(activity: Activity, challengePayload: ChallengePayload) {
        handleChallengeUseCase(activity, challengePayload) {
            with(it as Result.Success) { finalize(data) }
        }
    }

    companion object {

        @VisibleForTesting
        internal const val REQUEST_CODE_CARDINAL_CHALLENGE = 1

    }
}