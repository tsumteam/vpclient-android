package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MainScreenCategoryType {
    @SerialName("woman") WOMAN,
    @SerialName("man") MAN,
    @SerialName("child") CHILD
}
