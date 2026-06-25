package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressSuggestionResponse(
    @SerialName("title") val title: String? = null,
    @SerialName("coordinate") val coordinate: CoordinateResponse? = null
)
