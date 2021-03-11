/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.util

import android.os.Build

fun isEmulator(): Boolean {
    return checkFingerprint()
            || checkModel()
            || checkManufacturerBrandAndProduct()
            || checkHardware()
}

private fun checkFingerprint() = Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")

private fun checkModel(): Boolean {
    val modelStrings = listOf("google_sdk", "Emulator", "Android SDK built for x86")
    return modelStrings.filter { it in Build.MODEL }
        .isNotEmpty()
}

private fun checkManufacturerBrandAndProduct() = Build.MANUFACTURER.contains("Genymotion")
        || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
        || checkProduct()

private fun checkProduct(): Boolean {
    val productStrings =
        listOf("sdk_google", "google_sdk", "sdk", "sdk_x86", "vbox86p", "emulator", "simulator")
    return productStrings.filter { it in Build.PRODUCT }
        .isNotEmpty()
}

private fun checkHardware() = Build.HARDWARE.contains("goldfish")
        || Build.HARDWARE.contains("ranchu")
