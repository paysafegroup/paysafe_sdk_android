/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import android.app.Activity
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.cm.models.CardinalError
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalValidateReceiver
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.util.Result
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.util.toJson
import com.paysafe.util.createChallenge
import com.paysafe.whenEver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder

@RunWith(JUnit4::class)
class HandlerChallengeUseCase {

    private val api = mock<NetbanxApi>()
    private val cardinal = mock<Cardinal>()

    private val tested = HandleChallengeUseCase(api, cardinal)

    @Test
    fun `handleChallengeUseCase returns challenge data with status SUCCESSFUL`() {
        `handleChallengeUseCase returns challenge data with status for action code`(FinalizeStatus.SUCCESSFUL, CardinalActionCode.SUCCESS)
    }

    @Test
    fun `handleChallengeUseCase returns challenge data with status FAILED`() {
        `handleChallengeUseCase returns challenge data with status for action code`(FinalizeStatus.FAILED, CardinalActionCode.ERROR)
    }

    private fun `handleChallengeUseCase returns challenge data with status for action code`(status: FinalizeStatus, cardinalActionCode: CardinalActionCode) {
        // given
        val activity = mock<Activity>()

        val challenge = createChallenge()

        val mockCallback = mock<(Result<ChallengeData, ThreeDSecureError>) -> Unit>()

        val validateResponse = ValidateResponse(true, cardinalActionCode, CardinalError(0, ""))

        val serverJwt = "serverJwt"

        whenEver(
            cardinal.cca_continue(
                safeAny(String::class.java),
                safeAny(String::class.java),
                safeAny(Activity::class.java),
                safeAny()
            )
        ).thenAnswer {
            with(it.arguments[3] as CardinalValidateReceiver) { onValidated(activity, validateResponse, serverJwt) }
        }

        // when
        tested(activity, challenge, mockCallback)

        // then
        inOrder(api, mockCallback).apply {
            verify(api).log(safeEq(EventType.SUCCESS), safeAny())
            verify(mockCallback).invoke(
                safeEq(
                    Result.Success(
                        ChallengeData(
                            validateResponse.toJson(),
                            challenge.accountId,
                            challenge.authId,
                            serverJwt,
                            status
                        )
                    )
                )
            )
        }
    }
}