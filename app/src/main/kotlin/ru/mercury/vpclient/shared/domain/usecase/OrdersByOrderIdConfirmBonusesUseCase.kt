package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.OrderBonusReservationConfirmationRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdConfirmBonusesUseCase.Params
import javax.inject.Inject

// fixme
class OrdersByOrderIdConfirmBonusesUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        handleResponse(
            request = {
                val request = OrderBonusReservationConfirmationRequest(
                    smsCode = params.code
                )
                networkService.ordersByOrderIdConfirmBonuses(
                    orderId = params.orderId,
                    request = request
                )
            },
            onSuccess = {},
            onFailure = { error -> throw OrdersByOrderIdConfirmBonusesException(error.message) }
        )
    }

    data class Params(
        val orderId: String,
        val code: String
    )

    data class OrdersByOrderIdConfirmBonusesException(
        override val message: String
    ): ClientException(message)
}
