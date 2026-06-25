package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MyClientsFilterType {
    @SerialName("fitting") FITTING,
    @SerialName("basket") BASKET,
    @SerialName("order") ORDER,
    @SerialName("chat") CHAT,
    @SerialName("compilation") COMPILATION
}
