/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Error(
    val code: String = "",
    val message: String = "",
    val details: List<String> = listOf(),
    val fieldErrors: List<FieldError> = listOf(),
    val links: List<Link> = listOf()
) : Parcelable
