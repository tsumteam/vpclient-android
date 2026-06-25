package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EmployeeNotificationsFilterType {
    @SerialName("client") CLIENT,
    @SerialName("timestamp") TIMESTAMP,
    @SerialName("notificationType") NOTIFICATION_TYPE
}
