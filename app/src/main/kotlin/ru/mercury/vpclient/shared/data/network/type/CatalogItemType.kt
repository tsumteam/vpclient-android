package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CatalogItemType {
    @SerialName("none") NONE,
    @SerialName("man") MAN,
    @SerialName("woman") WOMAN,
    @SerialName("child") CHILD,
    @SerialName("all") ALL
}
