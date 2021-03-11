/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.data

import com.paysafe.BuildConfig

internal data class Sdk(
    val version: String = BuildConfig.VERSION_NAME,
    val type: String = "ANDROID"
)