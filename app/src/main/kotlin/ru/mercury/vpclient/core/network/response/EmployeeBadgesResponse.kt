package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeBadgesResponse(
    @SerialName("employeeId") val employeeId: String?,
    @SerialName("basketIcon") val basketIcon: EmployeeBadgeResponse?,
    @SerialName("fittingIcon") val fittingIcon: EmployeeBadgeResponse?,
    @SerialName("messengerIcon") val messengerIcon: EmployeeBadgeResponse?,
    @SerialName("orderIcon") val orderIcon: EmployeeBadgeResponse?,
    @SerialName("compilationIcon") val compilationIcon: EmployeeBadgeResponse?
)
