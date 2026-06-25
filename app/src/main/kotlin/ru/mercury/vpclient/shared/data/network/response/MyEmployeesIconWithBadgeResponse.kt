package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyEmployeesIconWithBadgeResponse(
    @SerialName("badge") val badge: Int? = null,
    @SerialName("icon") val icon: MyEmployeesIconResponse? = null
)
