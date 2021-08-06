/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo.googlepay

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.paysafe.customervault.CustomerVaultCallback
import com.paysafe.customervault.data.GooglePaySingleUseToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.paysafe.common.Error
import com.paysafe.demo.*
import com.paysafe.demo.data.authentications.AuthenticationRepository
import com.paysafe.demo.data.authentications.PaymentStatus
import com.paysafe.demo.data.authentications.Result

class GooglePayViewModel(application: Application) : AndroidViewModel(application) {

    private var customerVaultService = (application as DemoApplication).customerVaultService
    private val authenticationRepository = AuthenticationRepository()

    lateinit var paymentsClient: PaymentsClient

    private val _totalPrice = MutableLiveData<Int>()

    val totalPrice: LiveData<Int>
        get() = _totalPrice

    private val _paymentData = MutableLiveData<Event<PaymentData?>>()

    val paymentData: LiveData<Event<PaymentData?>>
        get() = _paymentData

    private val _isReadyToPay = MutableLiveData<Boolean>()

    val isReadyToPay: LiveData<Boolean>
        get() = _isReadyToPay

    private var _transactionStatus = MutableLiveData<Event<String?>>()

    val transactionStatus: LiveData<Event<String?>>
        get() = _transactionStatus

    private var _googlePaySingleUseTokenStatus = MutableLiveData<Event<GooglePaySingleUseToken?>>()

    val googlePaySingleUseTokenStatus: LiveData<Event<GooglePaySingleUseToken?>>
        get() = _googlePaySingleUseTokenStatus

    fun setTotalPrice(price: Int) {
        _totalPrice.value = price
    }

    fun setPaymentData(data: Event<PaymentData?>) {
        _paymentData.postValue(data)
    }

    fun setTransactionStatus(status: String){
        _transactionStatus.postValue(Event(status))
    }

    fun setGooglePaySingleUseTokenStatus(token: GooglePaySingleUseToken){
        _googlePaySingleUseTokenStatus.postValue(Event(token))
    }

    fun initializePaymentsClient(activity: Activity) {
        paymentsClient = Wallet.getPaymentsClient(
            activity, Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build()
        )
    }

    fun startIsReadyToPayTask() {
        paymentsClient.isReadyToPay(IsReadyToPayRequest.fromJson(GooglePayRequestHelper.getGooglePayBaseConfigurationJson()))
            .addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)?.let {
                        _isReadyToPay.postValue(it)
                    }
                } catch (exception: ApiException) {
                    _isReadyToPay.postValue(false)
                }
            }
    }

    fun pay(paymentData: PaymentData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val token = createToken(paymentData)
                authorize(token)
            }
        }
    }

    private suspend fun createToken(paymentData: PaymentData): String {
        return suspendCoroutine { continuation ->
            customerVaultService.createGooglePayPaymentToken(
                paymentData.toJson(), object : CustomerVaultCallback<GooglePaySingleUseToken> {

                    override fun onSuccess(token: GooglePaySingleUseToken) {
                        _googlePaySingleUseTokenStatus.postValue(Event(token))
                        continuation.resume(token.paymentToken)
                    }

                    override fun onError(error: Error) {
                        _transactionStatus.postValue(Event(PaymentStatus.FAILED.name))
                    }

                }
            )
        }
    }

    private suspend fun authorize(token: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (
                    val result = authenticationRepository.authorize(token, _totalPrice.value ?: 0)) {
                    is Result.Success -> _transactionStatus.postValue(Event(result.data.status))
                    is Result.Error -> _transactionStatus.postValue(Event(
                        PaymentStatus.FAILED.name))
                }
            }
        }
    }

}