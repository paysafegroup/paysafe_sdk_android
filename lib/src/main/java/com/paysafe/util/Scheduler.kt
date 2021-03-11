/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

import android.os.Handler
import android.os.Looper

internal interface Scheduler {

    fun post(r: () -> Unit): Unit

}

internal class MainThreadScheduler : Scheduler {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun post(r: () -> Unit) {
        mainThreadHandler.post(r)
    }


}