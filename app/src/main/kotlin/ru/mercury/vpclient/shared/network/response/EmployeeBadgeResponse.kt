package ru.mercury.vpclient.shared.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeBadgeResponse(
    @SerialName("badge") val badge: Int?,
    @SerialName("icon") val icon: EmployeeBadgeIconResponse?
)
