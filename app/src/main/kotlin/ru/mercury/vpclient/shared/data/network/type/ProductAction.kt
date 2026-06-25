package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProductAction {
    @SerialName("none") NONE,
    @SerialName("viewed") VIEWED,
    @SerialName("addedToBasket") ADDED_TO_BASKET,
    @SerialName("addedToFitting") ADDED_TO_FITTING,
    @SerialName("bought") BOUGHT
}
