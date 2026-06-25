package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.ClientAddressDtoResponse
import ru.mercury.vpclient.shared.data.network.response.CoordinateResponse

@Serializable
data class UpdateClientAddressRequest(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("address") val address: ClientAddressDtoResponse? = null,
    @SerialName("coordinate") val coordinate: CoordinateResponse? = null
)
