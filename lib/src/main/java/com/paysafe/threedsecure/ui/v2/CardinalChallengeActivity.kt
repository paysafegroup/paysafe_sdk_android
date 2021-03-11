/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui.v2

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.threedsecure.data.toIntent
import com.paysafe.threedsecure.ui.BaseActivity
import com.paysafe.threedsecure.util.Event

class CardinalChallengeActivity : BaseActivity() {

    private val viewModel by lazy { getViewModel<CardinalChallengeViewModel>() }

    private val resultObserver: Observer<Event<ChallengeResult>> = Observer { event ->
        event.getValueIfNotHandled()?.let {
            setResult(it.resultCode, it.toIntent())
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * We are going to leak on purpose here.
         * The problem is that Cardinal delivers the payload via a callback.
         * If the current activity observers based on it's lifecycle, we are going to lose
         * the data. This will keep us listening for updates as long as we need to.
         */
        viewModel.result.observeForever(resultObserver)

        if (savedInstanceState == null) {
            with(intent.getParcelableExtra<ChallengePayload>(EXTRA_CHALLENGE_PAYLOAD)) {
                viewModel.onValidateChallenge(
                    this@CardinalChallengeActivity,
                    this
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        viewModel.result.removeObserver(resultObserver)
    }

    companion object {

        private const val EXTRA_CHALLENGE_PAYLOAD = "EXTRA_CHALLENGE_PAYLOAD"

        internal fun createStartIntent(
            context: Context,
            challengePayload: ChallengePayload
        ) =
            with(
                Intent(context, CardinalChallengeActivity::class.java)
                    .putExtra(EXTRA_CHALLENGE_PAYLOAD, challengePayload)
            ) {
                PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
            }
    }
}