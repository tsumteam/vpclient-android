package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import ru.mercury.vpclient.shared.data.network.type.MainScreenLinkType

@Serializable
data class MainScreenItemResponse(
    @SerialName("linkType") val linkType: MainScreenLinkType? = null,
    @SerialName("bannerLinkUrl") val bannerLinkUrl: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("subtitle") val subtitle: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("catalogLink") val catalogLink: JsonObject? = null,
    @SerialName("fashionImageId") val fashionImageId: Int? = null,
    @SerialName("fashionImageTitle") val fashionImageTitle: String? = null,
    @SerialName("giftCard") val giftCard: GiftCardResponseItemResponse? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("searchCard") val searchCard: CatalogProductSearchCardResponse? = null,
    @SerialName("videoUrl") val videoUrl: String? = null,
    @SerialName("subtitleColorHex") val subtitleColorHex: String? = null,
    @SerialName("subtitleButton") val subtitleButton: String? = null,
    @SerialName("subtitleButtonColorHex") val subtitleButtonColorHex: String? = null
)
