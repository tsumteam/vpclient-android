package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CatalogCategoryType {
    @SerialName("catalog") CATALOG,
    @SerialName("giftCard") GIFT_CARD,
    @SerialName("action") ACTION
}
