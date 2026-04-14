package ru.mercury.vpclient.shared.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilteredProductsQuantityResponse(
    @SerialName("quantity") val quantity: Int? = null
)
