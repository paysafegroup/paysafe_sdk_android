/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
internal data class ChallengePayload(
    val authId: String,
    val accountId: String,
    val transactionId: String,
    val payload: String,
    val threeDSecureVersion: String
) : Parcelable

internal fun String.parseChallengePayload() =
    with(JSONObject(this)) {
        ChallengePayload(
            getString("id"),
            getString("accountId"),
            getString("transactionId"),
            getString("payload"),
            optString("threeDSecureVersion", "1.1.0")
        )
    }
