package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class System {
    @SerialName("tsum") TSUM,
    @SerialName("btq") BTQ
}
