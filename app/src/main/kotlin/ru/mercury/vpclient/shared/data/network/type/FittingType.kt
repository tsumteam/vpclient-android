package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FittingType {
    @SerialName("none") NONE,
    @SerialName("inTheStore") IN_THE_STORE,
    @SerialName("atHome") AT_HOME
}
