package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MessageReadType {
    @SerialName("none") NONE,
    @SerialName("received") RECEIVED,
    @SerialName("read") READ
}
