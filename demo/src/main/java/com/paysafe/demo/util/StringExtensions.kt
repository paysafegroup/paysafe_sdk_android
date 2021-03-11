/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.util

import java.lang.StringBuilder

fun String.formatAsCardNumber(): String {
    val builder = StringBuilder().apply {
        this@formatAsCardNumber.forEachIndexed { index, char ->
            if ((index + 1) % 4 == 0 && index != this@formatAsCardNumber.length - 1) {
                this.append("$char ")
            } else {
                this.append(char)
            }
        }
    }
    return builder.toString()
}