package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.ApproveFittingDeliveryRequestItemResponse

@Serializable
data class ApproveFittingDeliveryRequest(
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("actionType") val actionType: String? = null,
    @SerialName("comment") val comment: String? = null,
    @SerialName("items") val items: List<ApproveFittingDeliveryRequestItemResponse>? = null
)
