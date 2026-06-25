package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeCompilationProductsReportRequest(
    @SerialName("employeeId") val employeeId: String? = null
)
