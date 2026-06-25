package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BlvLinkContentType {
    @SerialName("basket") BASKET,
    @SerialName("fitting") FITTING,
    @SerialName("order") ORDER
}
