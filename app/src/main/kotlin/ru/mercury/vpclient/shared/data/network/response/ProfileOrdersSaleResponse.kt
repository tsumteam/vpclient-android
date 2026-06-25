package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileOrdersSaleResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("orderNumber") val orderNumber: String? = null,
    @SerialName("totalPrice") val totalPrice: Double? = null,
    @SerialName("isFinished") val isFinished: Boolean? = null,
    @SerialName("isFinishedAsString") val isFinishedAsString: String? = null,
    @SerialName("isDelivered") val isDelivered: Boolean? = null,
    @SerialName("isDeliveredAsString") val isDeliveredAsString: String? = null,
    @SerialName("paymentStatusAsString") val paymentStatusAsString: String? = null,
    @SerialName("imageUrls") val imageUrls: List<String>? = null,
    @SerialName("productsQty") val productsQty: Int? = null,
    @SerialName("salesType") val salesType: String? = null,
    @SerialName("isOnlinePayAvailable") val isOnlinePayAvailable: Boolean? = null
)
