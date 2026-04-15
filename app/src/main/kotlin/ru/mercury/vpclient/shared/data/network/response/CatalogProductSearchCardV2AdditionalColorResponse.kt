package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductSearchCardV2AdditionalColorResponse(
    @SerialName("name") val name: String?,
    @SerialName("photoUrl") val photoUrl: String?,
    @SerialName("order") val order: Int?
)
