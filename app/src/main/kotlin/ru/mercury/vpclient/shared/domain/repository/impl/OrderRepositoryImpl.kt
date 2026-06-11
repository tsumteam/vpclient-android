package ru.mercury.vpclient.shared.domain.repository.impl

import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetails
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.ProfileOrdersSalesRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.toProfileOrder
import ru.mercury.vpclient.shared.domain.mapper.toProfileOrderDetails
import ru.mercury.vpclient.shared.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): OrderRepository {

    override suspend fun profileOrders(clientId: String, limit: Int, offset: Int): List<ProfileOrder> {
        val response = handleResponseResult {
            val request = ProfileOrdersSalesRequest(
                clientId = clientId,
                limit = limit,
                offset = offset
            )
            networkService.sales(
                request = request,
                limit = limit,
                offset = offset
            )
        }.getOrThrow()

        return response.items.orEmpty().mapNotNull { item -> item.toProfileOrder() }
    }

    override suspend fun profileOrder(orderNumber: String): ProfileOrderDetails {
        val response = handleResponseResult {
            networkService.ordersByOrderId(orderId = orderNumber)
        }.getOrThrow()

        return response.toProfileOrderDetails()
    }
}
