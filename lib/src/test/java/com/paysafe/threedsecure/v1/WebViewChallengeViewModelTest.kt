/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.v1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.paysafe.*
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.util.Result
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.ui.v1.WebViewChallengeViewModel
import com.paysafe.threedsecure.util.Event
import com.paysafe.util.createChallenge
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class WebViewChallengeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val finalizeUseCase = mock<FinalizeUseCase>()

    private val tested = WebViewChallengeViewModel(finalizeUseCase)

    @Test
    fun `onChallengeValidated() with undefined error returns authentication ID`() {
        // given
        val authenticationId = "1234"
        val jsError = "undefined"

        val mockObserver = mock(Observer::class.java) as Observer<Event<ChallengeResult>>
        tested.result.observeForever(mockObserver)

        // when
        tested.onChallengeValidated(authenticationId, jsError)

        // then
        verify(mockObserver).onChanged(safeEq(Event(ChallengeResult.Success(authenticationId))))
    }

    @Test
    fun `onChallengeValidated() with error returns error payload`() {
        // given
        val expectedErrorCode = "9013"
        val expectedErrorMessage = "Message to display to the customer"
        val detailedMessage = "Detailed description of the error"

        val jsError = JSONObject()
            .put("code", expectedErrorCode)
            .put("displayMessage", expectedErrorMessage)
            .put("detailedMessage", detailedMessage)
            .toString()

        val mockObserver = mock(Observer::class.java) as Observer<Event<ChallengeResult>>
        tested.result.observeForever(mockObserver)

        // when
        tested.onChallengeValidated("", jsError)

        // then
        verify(mockObserver).onChanged(
            safeEq(
                Event(
                    ChallengeResult.Failure(
                        ThreeDSecureError(
                            code = expectedErrorCode,
                            displayMessage = expectedErrorMessage,
                            detailedMessage = detailedMessage
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `onMenuItemSelected() with action_cancelled finalizes the authentication and returns authentication ID`() {
        // given
        val challengePayload = createChallenge()

        whenEver(
            finalizeUseCase(
                safeEq(
                    ChallengeData(
                        null,
                        challengePayload.accountId,
                        challengePayload.authId,
                        null,
                        FinalizeStatus.CANCELLED
                    )
                ),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<String, ThreeDSecureError>) -> Unit) { this(Result.Success(challengePayload.authId)) }
        }

        val mockObserver = mock(Observer::class.java) as Observer<Event<ChallengeResult>>
        tested.result.observeForever(mockObserver)

        // when
        tested.onMenuItemSelected(R.id.action_cancel, challengePayload)

        // then
        verify(mockObserver).onChanged(
            safeEq(
                Event(
                    ChallengeResult.Success(challengePayload.authId)
                )
            )
        )
    }

    @Test
    fun `onMenuItemSelected() with action_cancelled finalizes the authentication and returns error`() {
        val error =
            ThreeDSecureError(code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR, detailedMessage = "message")

        val challengePayload = createChallenge()

        whenEver(
            finalizeUseCase(
                safeEq(
                    ChallengeData(
                        null,
                        challengePayload.accountId,
                        challengePayload.authId,
                        null,
                        FinalizeStatus.CANCELLED
                    )
                ),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[1] as (Result<String, ThreeDSecureError>) -> Unit) { this(Result.Failure(error)) }
        }

        val mockObserver = mock(Observer::class.java) as Observer<Event<ChallengeResult>>
        tested.result.observeForever(mockObserver)

        // when
        tested.onMenuItemSelected(R.id.action_cancel, challengePayload)

        // then
        verify(mockObserver).onChanged(
            safeEq(
                Event(
                    ChallengeResult.Failure(error)
                )
            )
        )
    }

    @Test
    fun `onMenuItemSelected() with unknown ID doesn't finalize the authentication`() {
        // given
        val challengePayload = createChallenge()
        // when
        tested.onMenuItemSelected(-1, challengePayload)

        // then
        verifyZeroInteractions(finalizeUseCase)
    }
}