/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.domain.HandleChallengeUseCase
import com.paysafe.threedsecure.domain.HandleSuccessfulChallengeUseCase
import com.paysafe.threedsecure.ui.v2.CardinalChallengeViewModel

open class BaseActivity : AppCompatActivity() {

    internal lateinit var cardinal: Cardinal
    internal lateinit var netbanxApi: NetbanxApi

    internal inline fun <reified VM : BaseViewModel> getViewModel() =
        ViewModelProvider(this, viewModelProviderFactory)[VM::class.java]

    private val viewModelProviderFactory: ViewModelProvider.Factory by lazy {

        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                with(modelClass) {
                    when {
                        isAssignableFrom(CardinalChallengeViewModel::class.java) ->
                            CardinalChallengeViewModel(
                                HandleChallengeUseCase(),
                                HandleSuccessfulChallengeUseCase(netbanxApi),
                                FinalizeUseCase(netbanxApi)
                            )

                        else -> throw IllegalArgumentException("Trying to create unknown model class ${modelClass.canonicalName}")
                    }
                } as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            netbanxApi = savedInstanceState.getRequiredParcelable(EXTRA_NETBANX_API)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(EXTRA_NETBANX_API, netbanxApi)
    }

    private inline fun <reified T : Parcelable> Bundle.getRequiredParcelable(key: String): T =
        when {
            Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelable(key) as? T
        }
            ?: throw IllegalStateException("Missing required parcelable extra with key $key")

    companion object {

        private const val EXTRA_NETBANX_API = "EXTRA_NETBANX_API"
    }
}
