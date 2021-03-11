/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

/**
 * Represents a 3DS flow error.
 * The [code] and [detailedMessage] contain additional information about the error,
 * [displayMessage] can be shown to the user and the [correlationId], when available,
 * can be provided to the Paysafe support team to help determine the cause of the error.
 */
@Parcelize
data class ThreeDSecureError(

    /**
     * The code of the error. Can be one of [ERROR_CODE_CONNECTION_FAILED], [ERROR_CODE_INVALID_MERCHANT_CONFIGURATION],
     * [ERROR_CODE_INVALID_API_KEY] or [ERROR_CODE_INTERNAL_SDK_ERROR].
     */
    val code: String,

    /**
     * Contains detailed information about the error. Not suitable to be shown to the user.
     */
    val detailedMessage: String = "There was an error $code, please contact our support",

    /**
     * Contains a user friendly error message.
     */
    val displayMessage: String = "There was an error $code, please contact our support",

    /**
     * A unique internal ID that is used for logging and tracking the flow progress. If available,
     * can be provided to the Paysafe support team during investigation.
     */
    val correlationId: String? = null
) : Parcelable {

    companion object {

        /**
         * The operation has failed due to a cancellation,
         * a time out or a connectivity issue.
         */
        const val ERROR_CODE_CONNECTION_FAILED = "9001"

        /**
         * The configuration of the provided merchant account is invalid.
         * The Paysafe support team should be notified.
         */
        const val ERROR_CODE_INVALID_MERCHANT_CONFIGURATION = "9501"

        /**
         * Invalid API key or API secret provided when creating a [PaysafeApiClient].
         */
        const val ERROR_CODE_INVALID_API_KEY = "9013"

        /**
         * A general SDK error. The [detailedMessage] should provide additional information.
         */
        const val ERROR_CODE_INTERNAL_SDK_ERROR = "9014"

    }

}

internal fun String.parseThreeDSecureError(): ThreeDSecureError? =
    if ("undefined" == this) {
        null
    } else {
        with(JSONObject(this)) {
            ThreeDSecureError(
                getString("code"),
                getString("detailedMessage"),
                getString("displayMessage"),
                optString("correlationId", null)
            )
        }
    }