package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoutiqueResponse(
    @SerialName("brand") val brand: String? = null,
    @SerialName("brandPhone") val brandPhone: String? = null
)
