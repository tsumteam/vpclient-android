package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyEmployees2Response(
    @SerialName("items") val items: List<MyEmployeesEmployeeResponse>? = null
)
