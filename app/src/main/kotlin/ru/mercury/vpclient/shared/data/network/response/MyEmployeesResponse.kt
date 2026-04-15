package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyEmployeesResponse(
    @SerialName("items") val items: List<EmployeeResponse> = emptyList()
)
