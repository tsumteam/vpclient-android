package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeLastActivityResponse(
    @SerialName("colorHex") val colorHex: String?,
    @SerialName("date") val date: String?
)
