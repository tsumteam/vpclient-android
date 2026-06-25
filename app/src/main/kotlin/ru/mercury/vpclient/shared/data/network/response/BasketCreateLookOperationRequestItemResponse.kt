package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType

@Serializable
data class BasketCreateLookOperationRequestItemResponse(
    @SerialName("operationType") val operationType: BasketOperationRequestType? = null,
    @SerialName("operationOrder") val operationOrder: Int? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("items") val items: List<BasketCreateLookOperationLineRequestItemResponse>? = null,
    @SerialName("lookId") val lookId: String? = null,
    @SerialName("name") val name: String? = null
)
