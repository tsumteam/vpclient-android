package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeBadgeIconResponse(
    @SerialName("colorHex") val colorHex: String?,
    @SerialName("isActive") val isActive: Boolean?,
    @SerialName("number") val number: Int?,
    @SerialName("type") val type: String?
)
