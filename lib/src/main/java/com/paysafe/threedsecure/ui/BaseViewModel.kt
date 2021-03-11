/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.util.Result
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.util.Event

internal open class BaseViewModel(private val finalizeUseCase: FinalizeUseCase) : ViewModel() {

    protected val _result = MutableLiveData<Event<ChallengeResult>>()
    val result: LiveData<Event<ChallengeResult>>
        get() = _result

    protected fun finalize(challengeData: ChallengeData) =
        finalizeUseCase(challengeData ) {
        when (it) {
            is Result.Success -> _result.value = Event(ChallengeResult.Success(challengeData.authenticationId))
            is Result.Failure -> _result.value = Event(ChallengeResult.Failure(it.error))
        }
    }
}