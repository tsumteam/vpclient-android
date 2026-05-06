package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.entity.ActivityCounterTypeRequestEnum

@Serializable
data class AggregatedActivityCounterResponseItem(
    @SerialName("type") val type: ActivityCounterTypeRequestEnum? = null,
    @SerialName("value") val value: Int? = null
)
