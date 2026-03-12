package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeBadgeIconResponse(
    @SerialName("colorHex") val colorHex: String?,
    @SerialName("isActive") val isActive: Boolean?,
    @SerialName("number") val number: Int?,
    @SerialName("type") val type: String?
)
