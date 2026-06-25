package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.System

@Serializable
data class ReserveRequest(
    @SerialName("serialId") val serialId: String? = null,
    @SerialName("locationId") val locationId: String? = null,
    @SerialName("system") val system: System? = null
)
