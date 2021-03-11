/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.util

import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

internal fun okHttpClient(block: OkHttpClient.Builder.() -> Unit) = OkHttpClient.Builder().apply(block).build()

internal fun OkHttpClient.Builder.basicAuth(block: OkHttpBasicAuthBuilder.() -> Unit) =
    OkHttpBasicAuthBuilder().apply(block).build().also { this.addInterceptor(it) }

internal class OkHttpBasicAuthBuilder {

    lateinit var username: String
    lateinit var password: String

    internal fun build() = object : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            return with(chain.request().newBuilder().addHeader("Authorization", Credentials.basic(username, password)).build()) {
                chain.proceed(this)
            }
        }

    }
}

internal fun OkHttpClient.Builder.logger(block: LoggerBuilder.() -> Unit) =
    LoggerBuilder().apply(block).build().also { this.addInterceptor(it) }

internal class LoggerBuilder {

    lateinit var tag: String
    lateinit var level: HttpLoggingInterceptor.Level

    internal fun build() = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {

        override fun log(message: String) {
            Log.d(tag, message)
        }

    }).apply {
        level = this@LoggerBuilder.level
    }
}