package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StatisticsType {
    @SerialName("none") NONE,
    @SerialName("byProducts") BY_PRODUCTS,
    @SerialName("byClients") BY_CLIENTS
}
