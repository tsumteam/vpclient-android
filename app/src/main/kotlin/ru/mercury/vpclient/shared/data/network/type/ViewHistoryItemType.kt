package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ViewHistoryItemType {
    @SerialName("wasViewedViaDetailCard") WAS_VIEWED_VIA_DETAIL_CARD,
    @SerialName("wasInBasketBefore") WAS_IN_BASKET_BEFORE,
    @SerialName("isInBasketNow") IS_IN_BASKET_NOW
}
