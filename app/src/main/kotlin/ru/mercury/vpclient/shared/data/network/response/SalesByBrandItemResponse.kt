package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SalesByBrandItemResponse(
    @SerialName("brand") val brand: String? = null,
    @SerialName("brandPercent") val brandPercent: Double? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("personalSales") val personalSales: Double? = null
)
