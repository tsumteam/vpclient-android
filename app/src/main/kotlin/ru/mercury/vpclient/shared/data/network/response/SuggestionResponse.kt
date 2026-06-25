package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestionResponse(
    @SerialName("items") val items: List<String>? = null,
    @SerialName("correction") val correction: String? = null
)
