package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.CheckoutBonusType
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.toCheckoutData
import ru.mercury.vpclient.shared.domain.usecase.OrderCheckoutDataUseCase.Params
import javax.inject.Inject

class OrderCheckoutDataUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<Params, FittingCheckoutData>(dispatchers.io) {

    override suspend fun execute(params: Params): FittingCheckoutData {
        val response = handleResponseResult {
            networkService.ordersByOrderId(orderId = params.orderNumber)
        }.getOrThrow()

        val order = response.order ?: throw ClientException("Заказ не найден")
        return order.toCheckoutData(bonusType = params.bonusType)
    }

    data class Params(
        val orderNumber: String,
        val bonusType: CheckoutBonusType
    )
}
