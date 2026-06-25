package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DateReceiptExpiredStatus {
    @SerialName("ok") OK,
    @SerialName("changed") CHANGED,
    @SerialName("overdue") OVERDUE
}
