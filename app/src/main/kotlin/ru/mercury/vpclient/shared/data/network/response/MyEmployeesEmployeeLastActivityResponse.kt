package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyEmployeesEmployeeLastActivityResponse(
    @SerialName("colorHex") val colorHex: String? = null,
    @SerialName("date") val date: String? = null
)
