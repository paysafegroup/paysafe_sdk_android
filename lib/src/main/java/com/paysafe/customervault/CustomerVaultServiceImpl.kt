/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.customervault

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.paysafe.customervault.data.GooglePaySingleUseToken
import com.paysafe.customervault.data.GooglePayTokenParams
import com.paysafe.customervault.data.SingleUseToken
import com.paysafe.customervault.data.SingleUseTokenParams
import com.paysafe.customervault.domain.CreateGooglePaySingleUseTokenUseCase
import com.paysafe.customervault.domain.CreateGooglePaySingleUseTokenWithFullPayloadUseCase
import com.paysafe.customervault.domain.CreateSingleUseTokenUseCase
import com.paysafe.util.MainThreadScheduler
import com.paysafe.util.Result
import com.paysafe.util.Scheduler


internal class CustomerVaultServiceImpl internal constructor(
    private val createSingleUseTokenUseCase: CreateSingleUseTokenUseCase,
    private val createGooglePaySingleUseTokenUseCase: CreateGooglePaySingleUseTokenUseCase,
    private val createGooglePaySingleUseTokenFullPayloadUseCase: CreateGooglePaySingleUseTokenWithFullPayloadUseCase,
    private val callbackScheduler: Scheduler = MainThreadScheduler()
) :
    CustomerVaultService {

    override fun createSingleUseToken(
        params: SingleUseTokenParams,
        callback: CustomerVaultCallback<SingleUseToken>
    ) {
        createSingleUseTokenUseCase(params) {
            when (it) {
                is Result.Success -> callbackScheduler.post { callback.onSuccess(it.data) }
                is Result.Failure -> callbackScheduler.post { callback.onError(it.error) }
            }
        }
    }

    override fun createGooglePayPaymentToken(
        params: GooglePayTokenParams,
        callback: CustomerVaultCallback<GooglePaySingleUseToken>
    ) {
        createGooglePaySingleUseTokenUseCase(params) {
            when (it) {
                is Result.Success -> callbackScheduler.post { callback.onSuccess(it.data) }
                is Result.Failure -> callbackScheduler.post { callback.onError(it.error) }
            }
        }
    }

    override fun createGooglePayPaymentToken(
        params: String,
        callback: CustomerVaultCallback<GooglePaySingleUseToken>
    ) {
        val jsonObj = Gson().fromJson(params, JsonObject::class.java)
        createGooglePaySingleUseTokenFullPayloadUseCase(jsonObj) {
            when (it) {
                is Result.Success -> callbackScheduler.post { callback.onSuccess(it.data) }
                is Result.Failure -> callbackScheduler.post { callback.onError(it.error) }
            }
        }
    }
}
