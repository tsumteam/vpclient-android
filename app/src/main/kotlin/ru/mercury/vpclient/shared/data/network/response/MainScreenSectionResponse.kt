package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MainScreenSectionResponse(
    @SerialName("sectionType") val sectionType: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("items") val items: List<MainScreenItemResponse> = emptyList(),
    @SerialName("titleCatalogLink") val titleCatalogLink: JsonObject? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("giftCards") val giftCards: GiftCardResponse? = null
)
