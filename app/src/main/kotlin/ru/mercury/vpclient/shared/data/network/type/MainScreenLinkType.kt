package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MainScreenLinkType {
    @SerialName("banner") BANNER,
    @SerialName("catalog") CATALOG,
    @SerialName("fashionImage") FASHION_IMAGE
}
