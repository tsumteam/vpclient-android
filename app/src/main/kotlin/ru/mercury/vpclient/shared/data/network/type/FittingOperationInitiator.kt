package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FittingOperationInitiator {
    @SerialName("client") CLIENT,
    @SerialName("employee") EMPLOYEE,
    @SerialName("system") SYSTEM,
    @SerialName("axapta") AXAPTA
}
