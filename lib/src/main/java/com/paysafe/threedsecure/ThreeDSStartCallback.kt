/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

interface ThreeDSStartCallback {

    /**
     * Called when the device fingerprint has been successfully generated.
     *
     * @param deviceFingerprintId the generated device fingerprint.
     */
    fun onSuccess(deviceFingerprintId: String)

    /**
     * Called when an error occurred while trying to generate a device fingerprint.
     *
     * @param error the resulting error.
     */
    fun onError(error: ThreeDSecureError)

}
