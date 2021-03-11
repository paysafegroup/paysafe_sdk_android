/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FieldError(
    val field: String = "",
    val error: String = ""
) : Parcelable
