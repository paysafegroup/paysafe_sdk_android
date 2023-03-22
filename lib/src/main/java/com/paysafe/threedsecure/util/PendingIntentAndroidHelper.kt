/*
 *  Copyright © Paysafe Holdings UK Limited 2023. For more information see LICENSE
 */

package com.paysafe.threedsecure.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

internal object PendingIntentAndroidHelper {

    @JvmStatic
    internal fun getActivity(context: Context, requestCode: Int, intent: Intent, flags: Int): PendingIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, requestCode, intent, flags or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, requestCode, intent, flags)
        }
}