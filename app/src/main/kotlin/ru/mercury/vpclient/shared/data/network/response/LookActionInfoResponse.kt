package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookActionInfoResponse(
    @SerialName("price") val price: Double? = null,
    @SerialName("priceWithoutDiscount") val priceWithoutDiscount: Double? = null,
    @SerialName("actionName") val actionName: String? = null,
    @SerialName("discountPercentage") val discountPercentage: Int? = null
)
