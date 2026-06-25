package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PayloadType {
    @SerialName("product") PRODUCT,
    @SerialName("compilationLook") COMPILATION_LOOK,
    @SerialName("clientCompilation") CLIENT_COMPILATION,
    @SerialName("basketLook") BASKET_LOOK,
    @SerialName("images") IMAGES,
    @SerialName("giftCard") GIFT_CARD
}
