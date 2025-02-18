/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    val rel: String = "",
    val href: String = ""
) : Parcelable
