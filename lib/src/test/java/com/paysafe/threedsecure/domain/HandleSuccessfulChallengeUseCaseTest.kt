/*
 *  Copyright © Paysafe Holdings UK Limited 2019. For more information see LICENSE
 */

package com.paysafe.threedsecure.domain

import com.cardinalcommerce.cardinalmobilesdk.cm.models.CardinalError
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.paysafe.mock
import com.paysafe.safeAny
import com.paysafe.safeEq
import com.paysafe.threedsecure.ThreeDSecureError
import com.paysafe.threedsecure.data.ChallengeData
import com.paysafe.threedsecure.data.EventType
import com.paysafe.threedsecure.data.FinalizeStatus
import com.paysafe.threedsecure.data.api.NetbanxApi
import com.paysafe.threedsecure.util.toJson
import com.paysafe.util.Result
import com.paysafe.util.createChallenge
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.inOrder


@RunWith(JUnit4::class)
class HandleSuccessfulChallengeUseCaseTest {

    private val api = mock<NetbanxApi>()

    private val tested = HandleSuccessfulChallengeUseCase(api)

    @Test
    fun `HandleSuccessfulChallengeUseCase returns challenge data with status SUCCESSFUL`() {
        `handleChallengeUseCase returns challenge data with status for action code`(
            FinalizeStatus.SUCCESSFUL,
            CardinalActionCode.SUCCESS
        )
    }

    @Test
    fun `HandleSuccessfulChallengeUseCase returns challenge data with status FAILED`() {
        `handleChallengeUseCase returns challenge data with status for action code`(
            FinalizeStatus.FAILED,
            CardinalActionCode.ERROR
        )
    }

    private fun `handleChallengeUseCase returns challenge data with status for action code`(
        status: FinalizeStatus,
        cardinalActionCode: CardinalActionCode
    ) {
        // given
        val challenge = createChallenge()
        val mockCallback = mock<(Result<ChallengeData, ThreeDSecureError>) -> Unit>()
        val validateResponse = ValidateResponse(true, cardinalActionCode, CardinalError(0, ""))
        val serverJwt = "serverJwt"

        // when
        tested(challenge, validateResponse, serverJwt, mockCallback)

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
