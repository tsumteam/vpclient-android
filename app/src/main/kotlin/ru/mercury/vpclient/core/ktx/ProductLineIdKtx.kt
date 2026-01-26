package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.ProductLineId
import ru.mercury.vpclient.core.network.request.OrderLineRequest
import ru.mercury.vpclient.core.network.response.OrderLineStatusResponse

val ProductLineId.orderLineRequest: OrderLineRequest
    get() = OrderLineRequest(
        lineId = this,
        actionType = null
    )

val ProductLineId.stayOrderLineRequest: OrderLineRequest
    get() = OrderLineRequest(
        lineId = this,
        actionType = OrderLineStatusResponse.ACTION_TYPE_STAY
    )
