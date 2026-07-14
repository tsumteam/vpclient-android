package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainScreenSectionsResponse(
    @SerialName("items") val items: List<MainScreenSectionResponse> = emptyList()
)
