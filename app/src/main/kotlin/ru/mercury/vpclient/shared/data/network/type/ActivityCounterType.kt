package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ActivityCounterType {
    @SerialName("basket") BASKET,
    @SerialName("messenger") MESSENGER,
    @SerialName("order") ORDER,
    @SerialName("fitting") FITTING,
    @SerialName("compilation") COMPILATION,
    @SerialName("axaptaNotification") AXAPTA_NOTIFICATION,
    @SerialName("clientNotification") CLIENT_NOTIFICATION,
    @SerialName("messages") MESSAGES
}
