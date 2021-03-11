/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

interface ThreeDSChallengeCallback {

    /**
     * Called when the challenge resolution has been created.
     *
     * @param challengeResolution the created challenge resolution.
     */
    fun onSuccess(challengeResolution: ChallengeResolution)

    /**
     * Called when an error occurred while trying to generate a device fingerprint.
     *
     * @param error the resulting error.
     */
    fun onError(error: ThreeDSecureError)

}