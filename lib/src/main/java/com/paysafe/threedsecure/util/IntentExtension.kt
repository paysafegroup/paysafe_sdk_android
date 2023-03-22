/*
 *  Copyright © Paysafe Holdings UK Limited 2023. For more information see LICENSE
 */

package com.paysafe.threedsecure.util

import android.content.Intent
import android.os.Parcelable

private const val DEFAULT_EXCEPTION_MESSAGE = "Missing mandatory extra!"

internal fun Intent.getRequiredStringExtra(
    key: String,
    illegalStateExceptionMessage: String = DEFAULT_EXCEPTION_MESSAGE
) = getStringExtra(key) ?: throw IllegalStateException(illegalStateExceptionMessage)

internal fun <T : Parcelable> Intent.getRequiredParcelableExtra(
    key: String,
    illegalStateExceptionMessage: String = DEFAULT_EXCEPTION_MESSAGE
) = getParcelableExtra<T>(key) ?: throw IllegalStateException(illegalStateExceptionMessage)