package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.ApproveFittingDeliveryManualPickupRequestItemResponse

@Serializable
data class ApproveFittingDeliveryManualPickupRequest(
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("actionType") val actionType: String? = null,
    @SerialName("items") val items: List<ApproveFittingDeliveryManualPickupRequestItemResponse>? = null
)
