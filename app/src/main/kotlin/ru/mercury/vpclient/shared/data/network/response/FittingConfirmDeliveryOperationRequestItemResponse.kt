package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FittingOperationRequestType

@Serializable
data class FittingConfirmDeliveryOperationRequestItemResponse(
    @SerialName("operationType") val operationType: FittingOperationRequestType? = null,
    @SerialName("operationOrder") val operationOrder: Int? = null,
    @SerialName("deliveryId") val deliveryId: String? = null
)
