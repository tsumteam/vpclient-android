package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.DeliveryType

@Serializable
data class OrderDeliveryResponse(
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("deliveryTime") val deliveryTime: DeliveryTimeResponse? = null,
    @SerialName("address") val address: OrderResponseAddressResponse? = null,
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("packOrderMyself") val packOrderMyself: Boolean? = null,
    @SerialName("manageClientCommunicationsMyself") val manageClientCommunicationsMyself: Boolean? = null,
    @SerialName("deliveryComment") val deliveryComment: String? = null,
    @SerialName("products") val products: List<OrderProductResponse>? = null,
    @SerialName("controls") val controls: OrderDeliveryResponseControlsResponse? = null
)
