/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.ui.v1

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.lifecycle.Observer
import com.paysafe.Environment
import com.paysafe.R
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.threedsecure.data.toIntent
import com.paysafe.threedsecure.ui.BaseActivity
import com.paysafe.threedsecure.ui.BaseViewModel
import com.paysafe.threedsecure.ui.ToolbarStyle
import com.paysafe.threedsecure.util.Event
import com.paysafe.util.base64Encode
import kotlinx.android.synthetic.main.activity_challenge.*

class WebViewChallengeActivity : BaseActivity() {

    private val viewModel by lazy { getViewModel<WebViewChallengeViewModel>() }

    private lateinit var challengePayload: ChallengePayload

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_challenge)
        setSupportActionBar(toolbar)

        intent.getParcelableExtra<ToolbarStyle>(EXTRA_TOOLBAR_CUSTOMIZATION)?.let {
            // Cardinal's ToolbarCustomization API doesn't really change toolbar text font and font size for 3ds 2.0 flow,
            // so we don't change them here as well
            toolbar.apply {
                setBackgroundColor(it.backgroundColor)
                title = it.headerText
                setTitleTextColor(it.textColor)
            }
        }

        val apiKey = intent.getStringExtra(EXTRA_API_KEY).replace("\n", "")
        val environment = intent.getStringExtra(EXTRA_ENVIRONMENT)
        val payloadString = intent.getStringExtra(EXTRA_SDK_PAYLOAD_STRING)

        challengePayload = intent.getParcelableExtra(EXTRA_SDK_CHALLENGE_PAYLOAD)

        webView.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = false
            settings.builtInZoomControls = true
            settings.allowUniversalAccessFromFileURLs = true
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                setInitialScale(100)
                settings.useWideViewPort = true
            }
            isHorizontalScrollBarEnabled = true
            isVerticalScrollBarEnabled = true

            addJavascriptInterface(
                JavaScriptInterface(),
                JS_INTERFACE_NAME
            )

            webViewClient = object : WebViewClient() {

                // Prevents infinite Javascript evaluation (observed on API 19)
                var javascriptEvaluated = false

                override fun onLoadResource(view: WebView?, url: String) {
                    if (url.contains("pareq.jsp") || url.contains("EAFService/jsp/v1/profile")) {
                        this@WebViewChallengeActivity.progressBar.hide()
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    this@WebViewChallengeActivity.progressBar.show()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (!javascriptEvaluated) {
                        view?.evaluateJavascript(
                            "paysafe.threedsecure.challenge(\"$apiKey\", {" +
                                    "environment: \"$environment\", " +
                                    "sdkChallengePayload: \"$payloadString\", " +
                                    "inlineSelector: \"challengeContainer\"" +
                                    "}, function (id, error) { $JS_INTERFACE_NAME.onValidated(id, JSON.stringify(error)) })",
                            null
                        )
                        javascriptEvaluated = true
                    }
                }

            }
        }

        if (savedInstanceState == null) {
            webView.loadUrl(getEnvironmentIndexHtml(environment))
        }

        observeChallengeResult(viewModel)
    }

    private fun observeChallengeResult(viewModel: BaseViewModel) {
        viewModel.result.observe(this,
            Observer<Event<ChallengeResult>> { event ->
                event.getValueIfNotHandled()?.let {
                    setResult(it.resultCode, it.toIntent())
                    finish()
                }
            })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let { webView.restoreState(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val toolbarStyle = intent.getParcelableExtra<ToolbarStyle>(EXTRA_TOOLBAR_CUSTOMIZATION)
        menuInflater.inflate(R.menu.menu_web_view_challenge_activity, menu)
        menu?.findItem(R.id.action_cancel)?.apply {
            actionView.setOnClickListener { onOptionsItemSelected(this) }
            toolbarStyle?.let {
                actionView.findViewById<TextView>(R.id.toolbarButton).text = it.buttonText
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (viewModel.onMenuItemSelected(item.itemId, challengePayload)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        viewModel.onMenuItemSelected(R.id.action_cancel, challengePayload)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onDestroy() {
        webView.removeJavascriptInterface(JS_INTERFACE_NAME)
        super.onDestroy()
    }

    private fun getEnvironmentIndexHtml(environment: String) =
        when (Environment.valueOf(environment)) {
            Environment.LIVE -> ENVIRONMENT_LIVE_HTML
            Environment.TEST -> ENVIRONMENT_TEST_HTML
        }

    private inner class JavaScriptInterface {

        @JavascriptInterface
        fun onValidated(authenticationId: String, errorJsonPayload: String) = runOnUiThread {
            viewModel.onChallengeValidated(authenticationId, errorJsonPayload)
        }

    }

    companion object {

        private const val JS_INTERFACE_NAME = "javaCallback"

        private const val EXTRA_API_KEY = "extra_api_key"
        private const val EXTRA_ENVIRONMENT = "extra_environment"
        private const val EXTRA_SDK_PAYLOAD_STRING = "extra_payload_string"
        private const val EXTRA_SDK_CHALLENGE_PAYLOAD = "extra_sdk_challenge_payload"
        private const val EXTRA_TOOLBAR_CUSTOMIZATION = "extra_toolbar_customization"

        private const val ENVIRONMENT_LIVE_HTML = "file:////android_asset/challenge_live_index.html"
        private const val ENVIRONMENT_TEST_HTML = "file:////android_asset/challenge_test_index.html"

        internal fun createStartIntent(
            context: Context,
            apiKey: String,
            apiSecret: String,
            environment: Environment,
            payloadString: String,
            challengePayload: ChallengePayload,
            toolbarStyle: ToolbarStyle?

        ) = with(
            Intent(context, WebViewChallengeActivity::class.java)
                .putExtra(EXTRA_API_KEY, "$apiKey:$apiSecret".base64Encode())
                .putExtra(EXTRA_ENVIRONMENT, environment.name)
                .putExtra(EXTRA_SDK_PAYLOAD_STRING, payloadString)
                .putExtra(EXTRA_SDK_CHALLENGE_PAYLOAD, challengePayload)
                .putExtra(EXTRA_TOOLBAR_CUSTOMIZATION, toolbarStyle)
        )
        { PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT) }
    }

}