/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.domain.HandleChallengeUseCase
import com.paysafe.threedsecure.ui.BaseActivity
import com.paysafe.threedsecure.util.ActivityLifecycleCallbacksAdapter
import com.paysafe.threedsecure.ui.v1.WebViewChallengeViewModel
import com.paysafe.threedsecure.ui.v2.CardinalChallengeViewModel

@Suppress("UNCHECKED_CAST")
internal class ViewModelProviderFactory(
    private val api: NetbanxApi,
    private val cardinal: Cardinal
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(WebViewChallengeViewModel::class.java) ->
                    WebViewChallengeViewModel(FinalizeUseCase(api))

                isAssignableFrom(CardinalChallengeViewModel::class.java) ->
                    CardinalChallengeViewModel(
                        HandleChallengeUseCase(api, cardinal),
                        FinalizeUseCase(api)
                    )
                else -> throw IllegalArgumentException("Trying to create unknown model class ${modelClass.canonicalName}")
            }
        } as T

    internal fun bindToActivityLifecycle(context: Context) =
        with(context.applicationContext as Application) {

            registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity is BaseActivity) {
                        activity.factory = this@ViewModelProviderFactory
                    }
                }

            })
        }
}