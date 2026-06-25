package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.ApproveFittingExpirationDateExtensionRequestItemResponse

@Serializable
data class ApproveFittingExpirationDateExtensionRequest(
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("actionType") val actionType: String? = null,
    @SerialName("qtyDaysExtension") val qtyDaysExtension: Int? = null,
    @SerialName("items") val items: List<ApproveFittingExpirationDateExtensionRequestItemResponse>? = null
)
