package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetails
import ru.mercury.vpclient.shared.domain.interactor.OrderInteractor
import ru.mercury.vpclient.shared.domain.repository.OrderRepository
import javax.inject.Inject

class OrderInteractorImpl @Inject constructor(
    private val dispatchers: SharedDispatchers,
    private val orderRepository: OrderRepository
): OrderInteractor {

    override suspend fun profileOrders(clientId: String, limit: Int, offset: Int): List<ProfileOrder> {
        return withContext(dispatchers.io) {
            orderRepository.profileOrders(
                clientId = clientId,
                limit = limit,
                offset = offset
            )
        }
    }

    override suspend fun profileOrder(orderNumber: String): ProfileOrderDetails {
        return withContext(dispatchers.io) {
            orderRepository.profileOrder(orderNumber = orderNumber)
        }
    }
}
