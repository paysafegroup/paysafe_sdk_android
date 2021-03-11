/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.v2

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.domain.HandleChallengeUseCase
import com.paysafe.threedsecure.ui.v2.CardinalChallengeViewModel
import com.paysafe.threedsecure.util.Event
import com.paysafe.util.Result
import com.paysafe.util.createChallenge
import com.paysafe.whenEver
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.*

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class CardinalChallengeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val handleChallengeUseCase = mock<HandleChallengeUseCase>()
    private val finalizeUseCase = mock<FinalizeUseCase>()

    private val tested =
        CardinalChallengeViewModel(handleChallengeUseCase, finalizeUseCase)

    @Test
    fun `onValidateChallenge() finalizes the authentication and returns the authentication ID`() {
        // given
        val expectedActivity = mock(Activity::class.java)
        val challenge = createChallenge()

        whenEver(handleChallengeUseCase(safeAny(), safeEq(challenge), safeAny())).thenAnswer {
            with(it.arguments[2] as (Result<ChallengeData, ThreeDSecureError>) -> Unit) {
                this(
                    Result.Success(
                        ChallengeData(
                            null,
                            challenge.accountId,
                            challenge.authId,
                            null,
                            FinalizeStatus.FAILED
                        )
                    )
                )
            }
        }

        whenEver(
            finalizeUseCase(safeAny(), safeAny())
        ).thenAnswer {
            with(it.arguments[1] as (Result<String, ThreeDSecureError>) -> Unit) {
                this(
                    Result.Success(
                        challenge.authId
                    )
                )
            }
        }

        val mockResultObserver = mock(Observer::class.java) as Observer<Event<ChallengeResult>>
        tested.result.observeForever(mockResultObserver)

        // when
        tested.onValidateChallenge(expectedActivity, challenge)

        // then
        verify(mockResultObserver).onChanged(safeEq(Event(ChallengeResult.Success(challenge.authId))))
    }

    @Test
    fun `onValidateChallenge() finalizes the authentication and returns error`() {
        // given
        val expectedActivity = mock(Activity::class.java)
        val challenge = createChallenge()

        whenEver(handleChallengeUseCase(safeAny(), safeEq(challenge), safeAny())).thenAnswer {
            with(it.arguments[2] as (Result<ChallengeData, ThreeDSecureError>) -> Unit) {
                this(
                    Result.Success(
                        ChallengeData(
                            null,
                            challenge.accountId,
                            challenge.authId,
                            null,
                            FinalizeStatus.FAILED
                        )
                    )
                )
            }
        }

        val error = ThreeDSecureError(
            code = ThreeDSecureError.ERROR_CODE_INTERNAL_SDK_ERROR,
            detailedMessage = "message"
        )
        whenEver(
            finalizeUseCase(safeAny(), safeAny())
        ).thenAnswer {
            with(it.arguments[1] as (Result<String, ThreeDSecureError>) -> Unit) {
                this(
                    Result.Failure(
                        error
                    )
                )
            }
        }

        val mockResultObserver = mock(Observer::class.java) as Observer<Event<ChallengeResult>>
        tested.result.observeForever(mockResultObserver)

        // when
        tested.onValidateChallenge(expectedActivity, challenge)

        // then
        verify(mockResultObserver).onChanged(safeEq(Event(ChallengeResult.Failure(error))))
    }

}