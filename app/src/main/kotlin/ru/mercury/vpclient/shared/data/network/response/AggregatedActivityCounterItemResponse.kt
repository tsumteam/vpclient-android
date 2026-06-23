package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType

@Serializable
data class AggregatedActivityCounterItemResponse(
    @SerialName("type") val type: ActivityCounterType? = null,
    @SerialName("value") val value: Int? = null
)
