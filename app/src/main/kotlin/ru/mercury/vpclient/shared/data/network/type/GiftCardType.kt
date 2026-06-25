package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GiftCardType {
    @SerialName("none") NONE,
    @SerialName("virtual") VIRTUAL,
    @SerialName("physical") PHYSICAL
}
