package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.EmployeeNotificationType

@Serializable
data class EmployeeNotificationsResponseItemResponse(
    @SerialName("type") val type: EmployeeNotificationType? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("deepLinkUrl") val deepLinkUrl: String? = null,
    @SerialName("timestamp") val timestamp: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("hasBadge") val hasBadge: Boolean? = null
)
