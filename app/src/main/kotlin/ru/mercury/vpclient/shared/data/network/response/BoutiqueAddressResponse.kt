package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoutiqueAddressResponse(
    @SerialName("boutiqueId") val boutiqueId: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("shortAddress") val shortAddress: String? = null,
    @SerialName("brandName") val brandName: String? = null
)
