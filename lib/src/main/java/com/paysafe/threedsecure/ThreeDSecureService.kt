/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.paysafe.Environment
import com.paysafe.PaysafeApiClient
import com.paysafe.PaysafeDsl
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.domain.ProcessPayloadUseCase
import com.paysafe.threedsecure.domain.StartUseCase
import com.paysafe.threedsecure.ui.UiStyle
import java.util.*



/**
 * The entry point of the 3DS 2.0 implementation.
 *
 * It's safe to use a single instance of the service to perform multiple 3DS 2.0 authentications.
 */
interface ThreeDSecureService {

    /**
     * Generates a device fingerprint for the customer, based on the provided [cardBin].
     *
     * This service will later call [callback]  with either a device fingerprint
     * or an error.
     *
     * @param cardBin required. The BIN of the card that is used in the authentication.
     */
    fun start(cardBin: String, callback: ThreeDSStartCallback)

    /**
     * Creates a [ChallengeResolution] for the provided [payload].
     *
     * This service will later call [callback] with either the [ChallengeResolution] or an error.
     *
     * You can launch the resolution by starting the [ChallengeResolution], providing either an activity or a fragment and a `requestCode`.
     *
     * The SDK will later call [Activity.onActivityResult] or [Fragment.onActivityResult] with the provided `requestCode`
     * to notify it that the challenge has been handled. You can extract the challenge result like this:
     * ```
     *  import com.paysafe.threedsecure.data.ChallengeResult.Companion.toChallengeResult
     *
     *  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
     *      super.onActivityResult(requestCode, resultCode, data)
     *
     *      if (<Your request code> == requestCode) {
     *          val challengeResult = data.toChallengeResult()
     *          when (challengeResult) {
     *              is ChallengeResult.Success -> // Use it.authenticationId to check the authentication status
     *              is ChallengeResult.Failure -> // Check it.error to get more information about the error
     *          }
     *      }
     *  }
     * ```
     * @param context the current application context.
     * @param payload the 3DS challenge payload. This should be returned by the backend.
     * @see ChallengeResolution
     */
    fun challenge(context: Context, payload: String, callback: ThreeDSChallengeCallback)

    @PaysafeDsl
    class Builder {

        /**
         * The current application context.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var context: Context

        /**
         * The client used for connecting to the Paysafe APIs.
         */
        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        lateinit var apiClient: PaysafeApiClient

        @field:JvmSynthetic
        @get:JvmSynthetic
        @set:JvmSynthetic
        var uiStyle: UiStyle = UiStyle()

        /**
         * Sets ThreeDSecureService context
         * Used for better readability when building client in java code
         */
        fun withContext(context: Context) = also { this.context = context }

        /**
         * Sets apiClient api secret
         * Used for better readability when building client in java code
         */
        fun withApiClient(apiClient: PaysafeApiClient) = also { this.apiClient = apiClient }

        /**
         * Sets apiClient account id
         * Used for better readability when building client in java code
         */
        fun withUiStyle(uiStyle: UiStyle) = also { this.uiStyle = uiStyle }


        fun build(): ThreeDSecureService {
            check(::context.isInitialized) { "Context is required, but missing" }
            check(::apiClient.isInitialized) { "API Client is required, but missing" }

            val cardinal = cardinal {
                context = this@Builder.context
                environment =
                    if (this@Builder.apiClient.environment == Environment.TEST) CardinalEnvironment.STAGING else CardinalEnvironment.PRODUCTION
                timeout = 8000L
                uiStyle {
                    this@Builder.uiStyle.toolbarStyle?.let {
                        toolbarStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            backgroundColor = this@Builder.getColorHex(it.backgroundColor)
                            buttonText = it.buttonText
                            headerText = it.headerText
                        }
                    }
                    this@Builder.uiStyle.labelStyle?.let {
                        labelStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            headingTextColor = this@Builder.getColorHex(it.headingTextColor)
                            headingTextFontName = it.headingTextFontName
                            headingTextFontSize = it.headingTextFontSize
                        }
                    }
                    this@Builder.uiStyle.textBoxStyle?.let {
                        textBoxStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            borderColor = this@Builder.getColorHex(it.borderColor)
                            borderWidth = it.borderWidth
                            cornerRadius = it.cornerRadius
                        }
                    }
                    this@Builder.uiStyle.nextButtonStyle?.let {
                        nextButtonStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            backgroundColor = this@Builder.getColorHex(it.backgroundColor)
                            cornerRadius = it.cornerRadius
                        }
                    }
                    this@Builder.uiStyle.cancelButtonStyle?.let {
                        cancelButtonStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            backgroundColor = this@Builder.getColorHex(it.backgroundColor)
                            cornerRadius = it.cornerRadius
                        }
                    }
                    this@Builder.uiStyle.resendButtonStyle?.let {
                        resendButtonStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            backgroundColor = this@Builder.getColorHex(it.backgroundColor)
                            cornerRadius = it.cornerRadius
                        }
                    }
                    this@Builder.uiStyle.continueButtonStyle?.let {
                        continueButtonStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            backgroundColor = this@Builder.getColorHex(it.backgroundColor)
                            cornerRadius = it.cornerRadius
                        }
                    }
                    this@Builder.uiStyle.verifyButtonStyle?.let {
                        verifyButtonStyle {
                            textColor = this@Builder.getColorHex(it.textColor)
                            textFontSize = it.textFontSize
                            textFontName = it.textFontName
                            backgroundColor = this@Builder.getColorHex(it.backgroundColor)
                            cornerRadius = it.cornerRadius
                        }
                    }
                }
            }

            val api = NetbanxApi(apiClient) {
                UUID.randomUUID().toString()
            }.also { ViewModelProviderFactory(it, cardinal).bindToActivityLifecycle(context) }

            return ThreeDSecureServiceCardinalImpl(
                StartUseCase(api, cardinal),
                ProcessPayloadUseCase(),
                apiClient.keyId,
                apiClient.keyPassword,
                apiClient.environment,
                uiStyle
            )
        }

        /**
         * Transform color int to hex color without transparency
         * if color is not valid fallback to grey
         */
        private fun getColorHex(color: Int): String{
            val colorHex = Integer.toHexString(color)
            return if (colorHex.length == 8){
                "#" + colorHex.substring(2)
            } else {
                "#CCCCCC"
            }
        }

    }

}