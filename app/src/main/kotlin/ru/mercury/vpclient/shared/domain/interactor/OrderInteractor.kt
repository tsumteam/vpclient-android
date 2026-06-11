package ru.mercury.vpclient.shared.domain.interactor

import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetails

interface OrderInteractor {

    suspend fun profileOrders(clientId: String, limit: Int, offset: Int): List<ProfileOrder>

    suspend fun profileOrder(orderNumber: String): ProfileOrderDetails
}
