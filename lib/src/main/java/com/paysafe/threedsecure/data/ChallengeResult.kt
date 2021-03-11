/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

import android.app.Activity
import android.content.Intent
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.ThreeDSecureService

private const val EXTRA_RESULT_CODE = "com.paysafe.threedsecure.EXTRA_RESULT_CODE"
private const val EXTRA_AUTHENTICATION_ID = "com.paysafe.threedsecure.EXTRA_AUTHENTICATION_ID"
private const val EXTRA_AUTHENTICATION_ERROR = "com.paysafe.threedsecure.EXTRA_AUTHENTICATION_ERROR"

/**
 * Represents a 3DS challenge result.
 *
 * An instance of this class is returned to the calling activity's [Activity.onActivityResult] method
 * when [ThreeDSecureService.challenge] has completed.
 *
 * @see ThreeDSecureService.challenge
 */
sealed class ChallengeResult(val resultCode: Int) {

    /**
     * The status of the challenge.
     *
     * If `true`, the challenge has been successfully resolved and you can get the [authenticationId]
     * to check the authentication status against the backend.
     *
     * if `false`, the challenge has not been resolved and you can use [error] to get more details
     * about the problem.
     *
     * Exists solely for easier Java interoperability.
     */
    val isSuccessful = Activity.RESULT_OK == resultCode

    open val authenticationId: String? = null
    open val error: ThreeDSecureError? = null

    data class Success(override val authenticationId: String) : ChallengeResult(resultCode = Activity.RESULT_OK)

    data class Failure(override val error: ThreeDSecureError) : ChallengeResult(resultCode = Activity.RESULT_CANCELED)

    companion object {
        /**
         * Intent extension method that transform intent to ChallengeResult object
         * Method usage in Java: ChallengeResult.fromIntent(intent)
         * Method usage in Kotlin:
         * 1. import com.paysafe.threedsecure.data.ChallengeResult.Companion.toChallengeResult
         * 2. intent.toChallengeResult()
         */
        @JvmStatic
        @JvmName("fromIntent")
        fun Intent.toChallengeResult() =
            when (getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)) {
                Activity.RESULT_OK -> Success(getStringExtra(EXTRA_AUTHENTICATION_ID))
                else -> Failure(getParcelableExtra(EXTRA_AUTHENTICATION_ERROR))
            }

    }
}

internal fun ChallengeResult.toIntent(): Intent = with(Intent().putExtra(EXTRA_RESULT_CODE, resultCode)) {
    when (resultCode) {
        Activity.RESULT_OK -> putExtra(EXTRA_AUTHENTICATION_ID, authenticationId)
        else -> putExtra(EXTRA_AUTHENTICATION_ERROR, error)
    }
}