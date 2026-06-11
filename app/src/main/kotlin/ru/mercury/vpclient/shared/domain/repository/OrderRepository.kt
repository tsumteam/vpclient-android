package ru.mercury.vpclient.shared.domain.repository

import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetails

interface OrderRepository {

    suspend fun profileOrders(clientId: String, limit: Int, offset: Int): List<ProfileOrder>

    suspend fun profileOrder(orderNumber: String): ProfileOrderDetails
}
