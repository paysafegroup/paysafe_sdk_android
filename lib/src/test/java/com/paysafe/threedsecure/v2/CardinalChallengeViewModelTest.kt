/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.v2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalChallengeObserver
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.ChallengePayload
import com.paysafe.threedsecure.data.ChallengeResult
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.threedsecure.domain.FinalizeUseCase
import com.paysafe.threedsecure.domain.HandleChallengeUseCase
import com.paysafe.threedsecure.domain.HandleSuccessfulChallengeUseCase
import com.paysafe.threedsecure.ui.v2.CardinalChallengeViewModel
import com.paysafe.threedsecure.util.Event
import com.paysafe.util.Result
import com.paysafe.util.createChallenge
import com.paysafe.whenEver
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@Suppress("UNCHECKED_CAST")
@RunWith(JUnit4::class)
class CardinalChallengeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val handleChallengeUseCase = mock<HandleChallengeUseCase>()
    private val handleSuccessfulChallengeUseCase = mock<HandleSuccessfulChallengeUseCase>()
    private val finalizeUseCase = mock<FinalizeUseCase>()
    private val cardinal = mock<Cardinal>()

    private val tested =
        CardinalChallengeViewModel(handleChallengeUseCase, handleSuccessfulChallengeUseCase, finalizeUseCase)

    @Test
    fun `handleChallenge() calls the handleChallengeUseCase with correct parameters`() {
        // given
        val cardinalChallengeObserver = mock<CardinalChallengeObserver>()
        val challengePayload = ChallengePayload(
            authId = "anyAuthId",
            accountId = "anyAccountId",
            transactionId = "anyTransactionId",
            payload = "any",
            threeDSecureVersion = "2.0",
        )

        // when
        tested.handleChallenge(cardinalChallengeObserver, challengePayload, cardinal)

        // then
        verify(handleChallengeUseCase).invoke(
            cardinalChallengeObserver,
            challengePayload,
            cardinal
        )
    }

    @Test
    fun `onChallengePassed() finalizes the authentication and returns the authentication ID`() {
        // given
        val challenge = createChallenge()
        val serverJwt = "anyServerJwt"
        val challengePayload = ChallengePayload(
            authId = challenge.authId,
            accountId = challenge.accountId,
            transactionId = challenge.transactionId,
            payload = "any",
            threeDSecureVersion = "2.0",
        )
        val validateResponse = mock<ValidateResponse>()

        whenEver(handleSuccessfulChallengeUseCase.invoke(safeEq(challengePayload), safeEq(validateResponse), safeEq(serverJwt), safeAny())).thenAnswer {
            with(it.arguments[3] as (Result<ChallengeData, ThreeDSecureError>) -> Unit) {
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
        tested.onChallengePassed(challengePayload, validateResponse, serverJwt)

        // then
        verify(mockResultObserver).onChanged(safeEq(Event(ChallengeResult.Success(challenge.authId))))
    }

    @Test
    fun `onChallengePassed() finalizes the authentication and returns error`() {
        // given
        val challenge = createChallenge()
        val serverJwt = "anyServerJwt"
        val challengePayload = ChallengePayload(
            authId = challenge.authId,
            accountId = challenge.accountId,
            transactionId = challenge.transactionId,
            payload = "any",
            threeDSecureVersion = "2.0",
        )
        val validateResponse = mock<ValidateResponse>()

        whenEver(handleSuccessfulChallengeUseCase.invoke(safeEq(challengePayload), safeEq(validateResponse), safeEq(serverJwt), safeAny())).thenAnswer {
            with(it.arguments[3] as (Result<ChallengeData, ThreeDSecureError>) -> Unit) {
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
        tested.onChallengePassed(challengePayload, validateResponse, serverJwt)

        // then
        verify(mockResultObserver).onChanged(safeEq(Event(ChallengeResult.Failure(error))))
    }

}