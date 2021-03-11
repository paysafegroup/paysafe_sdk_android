/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

import okhttp3.Headers

internal fun Headers.toMap() : Map<String, String> {
    val result = mutableMapOf<String, String>()
    for (i in 0 until size()) {
        result[name(i)] = value(i)
    }
    return result
}