package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeLastActivityResponse(
    @SerialName("colorHex") val colorHex: String?,
    @SerialName("date") val date: String?
)
