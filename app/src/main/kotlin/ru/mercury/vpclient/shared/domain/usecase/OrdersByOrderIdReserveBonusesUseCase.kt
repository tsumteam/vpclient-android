package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.OrderBonusReservationRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdReserveBonusesUseCase.Params
import javax.inject.Inject

// fixme
class OrdersByOrderIdReserveBonusesUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        handleResponse(
            request = {
                val request = OrderBonusReservationRequest(
                    bonusAmount = params.bonusAmount
                )
                networkService.ordersByOrderIdReserveBonuses(
                    orderId = params.orderId,
                    request = request
                )
            },
            onSuccess = {},
            onFailure = { error -> throw OrdersByOrderIdReserveBonusesException(error.message) }
        )
    }

    data class Params(
        val orderId: String,
        val bonusAmount: Double
    )

    data class OrdersByOrderIdReserveBonusesException(
        override val message: String
    ): ClientException(message)
}
