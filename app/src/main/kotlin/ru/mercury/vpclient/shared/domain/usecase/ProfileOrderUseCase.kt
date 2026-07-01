@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetails
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.toProfileOrderDetails
import javax.inject.Inject

// fixme
class ProfileOrderUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, ProfileOrderDetails>(dispatchers.io) {

    override suspend fun execute(orderNumber: String): ProfileOrderDetails {
        val response = handleResponseResult {
            networkService.ordersByOrderId(orderId = orderNumber)
        }.getOrThrow()

        return response.toProfileOrderDetails()
    }
}
