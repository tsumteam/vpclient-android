package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType

@Serializable
data class ResetActivityCountersRequest(
    @SerialName("type") val type: ActivityCounterType? = null
)
