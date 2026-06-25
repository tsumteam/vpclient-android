package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyEmployeesEmployeeBadgesResponse(
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("basketIcon") val basketIcon: MyEmployeesIconWithBadgeResponse? = null,
    @SerialName("fittingIcon") val fittingIcon: MyEmployeesIconWithBadgeResponse? = null,
    @SerialName("messengerIcon") val messengerIcon: MyEmployeesIconWithBadgeResponse? = null,
    @SerialName("orderIcon") val orderIcon: MyEmployeesIconWithBadgeResponse? = null,
    @SerialName("compilationIcon") val compilationIcon: MyEmployeesIconWithBadgeResponse? = null
)
