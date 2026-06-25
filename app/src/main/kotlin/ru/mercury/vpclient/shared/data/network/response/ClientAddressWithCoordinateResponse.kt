package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientAddressWithCoordinateResponse(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("addressId") val addressId: Int? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("flat") val flat: String? = null,
    @SerialName("entrance") val entrance: String? = null,
    @SerialName("intercom") val intercom: String? = null,
    @SerialName("floor") val floor: String? = null,
    @SerialName("comment") val comment: String? = null
)
