package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckOutAddressesDtoResponse(
    @SerialName("clientAddress") val clientAddress: ClientAddressWithCoordinateResponse? = null,
    @SerialName("boutiqueAddress") val boutiqueAddress: BoutiqueAddressResponse? = null,
    @SerialName("controls") val controls: CheckOutAddressesControlResponse? = null
)
