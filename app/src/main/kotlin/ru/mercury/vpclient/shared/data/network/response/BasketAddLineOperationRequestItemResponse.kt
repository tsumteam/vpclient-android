package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType

@Serializable
data class BasketAddLineOperationRequestItemResponse(
    @SerialName("operationType") val operationType: BasketOperationRequestType? = null,
    @SerialName("operationOrder") val operationOrder: Int? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("lineId") val lineId: String? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("compilationLookProductId") val compilationLookProductId: Int? = null
)
