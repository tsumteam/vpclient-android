package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FittingHistoryFilterType {
    @SerialName("operationType") OPERATION_TYPE,
    @SerialName("timestamp") TIMESTAMP
}
