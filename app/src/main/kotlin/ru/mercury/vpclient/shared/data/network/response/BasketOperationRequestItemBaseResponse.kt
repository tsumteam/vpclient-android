package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType

@Serializable
data class BasketOperationRequestItemBaseResponse(
    @SerialName("operationType") val operationType: BasketOperationRequestType? = null,
    @SerialName("operationOrder") val operationOrder: Int? = null
)
