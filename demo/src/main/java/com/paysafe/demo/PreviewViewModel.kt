/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.demo

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.paysafe.common.Error
import com.paysafe.customervault.CustomerVaultCallback
import com.paysafe.customervault.data.*
import com.paysafe.demo.data.Address
import com.paysafe.demo.data.cards.PaymentCard
import com.paysafe.demo.data.cards.CardsRepository
import com.paysafe.demo.data.authentications.AuthenticationRepository
import com.paysafe.demo.data.authentications.PaymentStatus
import com.paysafe.demo.data.authentications.Result
import com.paysafe.demo.store.PaymentOption
import com.paysafe.threedsecure.ChallengeResolution
import com.paysafe.threedsecure.ThreeDSChallengeCallback
import com.paysafe.threedsecure.ThreeDSStartCallback
import com.paysafe.threedsecure.ThreeDSecureError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PreviewViewModel(application: Application) : AndroidViewModel(application) {

    private var threeDService = (application as DemoApplication).threeDSecureService
    private var customerVaultService = (application as DemoApplication).customerVaultService
    private val authenticationRepository =
        AuthenticationRepository()
    private val cardsRepository = CardsRepository

    private val _totalPrice = MutableLiveData<Int>()

    val totalPrice: LiveData<Int>
        get() = _totalPrice

    private val _paymentMethod = MutableLiveData<PaymentOption>()

    val cards by lazy { cardsRepository.loadAllCards() }

    private val _card = MutableLiveData<PaymentCard>()

    val card: LiveData<PaymentCard>
        get() = _card

    private val _address = MutableLiveData<Address>()

    val address: LiveData<Address>
        get() = _address

    private var _transactionStatus = MutableLiveData<Event<String?>>()

    val transactionStatus: LiveData<Event<String?>>
        get() = _transactionStatus

    private var _challengeResolution = MutableLiveData<Event<ChallengeResolution?>>()

    val challengeResolution: LiveData<Event<ChallengeResolution?>>
        get() = _challengeResolution

    private var _googlePaySingleUseTokenStatus = MutableLiveData<Event<GooglePaySingleUseToken?>>()

    val googlePaySingleUseTokenStatus: LiveData<Event<GooglePaySingleUseToken?>>
        get() = _googlePaySingleUseTokenStatus

    fun saveNewCard(card: PaymentCard) {
        cardsRepository.addCard(card)
    }

    fun saveAddress(address: Address) {
        _address.value = address
    }

    fun setTotalPrice(price: Int) {
        _totalPrice.value = price
    }

    fun setCard(card: PaymentCard) {
        _card.value = card
    }

    fun setPaymentMethod(method: String){
        _paymentMethod.value = PaymentOption.valueOf(method)
    }

    fun selectedCardAndAddress() = card.value != null && address.value != null

    fun pay() {
        when(_paymentMethod.value){
            PaymentOption.S3D -> process3DSPayment()
            else -> processCustomerVaultPayment()
        }
    }

    private fun process3DSPayment(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val deviceFingerprint = start3ds(getCardBin())
                val authResponse = authenticationRepository.getChallengePayload(
                    totalPrice.value ?: 0,
                    card.value?.number ?: "",
                    formatExpMonth(),
                    formatExpYear(),
                    deviceFingerprint
                )
                when (authResponse) {
                    is Result.Success -> {
                        if (!authResponse.data.sdkChallengePayload.isNullOrEmpty()) {
                            challenge3ds(authResponse.data.sdkChallengePayload)
                        } else {
                            _transactionStatus.postValue(Event(authResponse.data.status))
                        }
                    }
                    // Handle error appropriately
                    // The following handling is just for demo purposes
                    is Result.Error -> _transactionStatus.postValue(Event(
                        PaymentStatus.FAILED.name))
                }
            }
        }
    }

    private fun processCustomerVaultPayment() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val token = createToken()
                authorize(token)
            }
        }
    }

    private suspend fun createToken(): String {
        return suspendCoroutine { continuation ->
            customerVaultService.createSingleUseToken(

                SingleUseTokenParams.Builder()
                    .withCard( SingleUseTokenParams.Card.Builder()
                        .withHolderName("")
                        .withCardNumber(_card.value?.number ?: "")
                        .withCvv(if (_card.value?.cvv.isNullOrEmpty()) null else _card.value?.cvv)
                        .withCardExpiry( CardExpiry.Builder()
                            .withMonth(formatExpMonth())
                            .withYear(formatExpYear())
                            .build()
                        )
                        .withBillingAddress( BillingAddress.Builder()
                            .onStreet(_address.value?.addressLine1 ?: "")
                            .onStreet2(_address.value?.addressLine2 ?: "")
                            .inCity(_address.value?.city ?: "")
                            .inCountry("BG")
                            .inState(_address.value?.state ?: "")
                            .withZip(_address.value?.postCode ?: "")
                            .build())
                        .build())
                    .build(), object : CustomerVaultCallback<SingleUseToken> {

                    override fun onSuccess(token: SingleUseToken) {
                        continuation.resume(token.paymentToken)
                    }

                    override fun onError(error: Error) {
                        // Handle error appropriately
                        // The following handling is just for demo purposes
                        _transactionStatus.postValue(Event(
                            PaymentStatus.FAILED.name))
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

    private suspend fun start3ds(cardBin: String): String {
        return suspendCoroutine { continuation ->
            threeDService.start(cardBin, object : ThreeDSStartCallback {

                override fun onSuccess(deviceFingerprintId: String) {
                    continuation.resume(deviceFingerprintId)
                }

                override fun onError(error: ThreeDSecureError) {
                    _transactionStatus.postValue(Event(PaymentStatus.FAILED.name))
                }

            })
        }
    }

    private fun challenge3ds(sdkChallengePayload: String) {
        threeDService.challenge(
            getApplication(),
            sdkChallengePayload,
            object : ThreeDSChallengeCallback {
                override fun onSuccess(challengeResolution: ChallengeResolution) {
                    _challengeResolution.postValue(Event(challengeResolution))
                }

                override fun onError(error: ThreeDSecureError) {
                    _transactionStatus.postValue(Event(PaymentStatus.FAILED.name))
                }
            })
    }

    fun lookup(authenticationId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val result =
                    authenticationRepository.authenticationsLookup(authenticationId)) {
                    is Result.Success -> _transactionStatus.postValue(Event(result.data.status))
                    is Result.Error -> _transactionStatus.postValue(Event(
                        PaymentStatus.FAILED.name))
                }
            }
        }
    }

    private fun getCardBin() = card.value?.number?.substring(CARD_BIN_START_INDEX, CARD_BIN_END_INDEX) ?: ""

    private fun formatExpMonth() = card.value?.expDate?.split(EXP_DATE_DELIMITER)?.get(0)?.toInt() ?: 0

    private fun formatExpYear() = EXP_YEAR_BASE + (card.value?.expDate?.split(EXP_DATE_DELIMITER)?.get(1)?.toInt() ?: 0)

    companion object {
        private const val EXP_YEAR_BASE = 2000
        private const val CARD_BIN_START_INDEX = 0
        private const val CARD_BIN_END_INDEX = 6
        private const val EXP_DATE_DELIMITER = "/"
    }

}