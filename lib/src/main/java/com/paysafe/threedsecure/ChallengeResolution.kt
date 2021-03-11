/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import android.app.Activity
import android.app.PendingIntent
import androidx.fragment.app.Fragment
import com.paysafe.threedsecure.data.ChallengeResult

/**
 * The resolution of a 3DS challenge.
 * Can be started by an activity, a fragment, or directly using [resolutionIntent].
 */
open class ChallengeResolution internal constructor(
    /**
     * The resolution's intent
     */
    val resolutionIntent: PendingIntent) {

    /**
     * Start the resolution from the provided [activity] with the specified [requestCode].
     *
     * The result will be delivered in [Activity.onActivityResult] with the specified [requestCode].
     *
     * @param activity the current activity.
     * @param requestCode the request code.
     * @see ChallengeResult
     */
    fun startForResult(activity: Activity, requestCode: Int) {
        activity.startIntentSenderForResult(resolutionIntent.intentSender, requestCode, null, 0, 0, 0)
    }

    /**
     * Start the resolution from the provided [fragment] with the specified [requestCode].
     *
     * The result will be delivered in [Fragment.onActivityResult] with the specified [requestCode].
     *
     * @param fragment the current fragment.
     * @param requestCode the request code.
     * @see ChallengeResult
     */
    fun startForResult(fragment: Fragment, requestCode: Int) {
        fragment.startIntentSenderForResult(resolutionIntent.intentSender, requestCode, null, 0, 0, 0, null)
    }
}