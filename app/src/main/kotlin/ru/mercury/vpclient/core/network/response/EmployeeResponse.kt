package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeResponse(
    @SerialName("employeeEmail") val employeeEmail: String?,
    @SerialName("employeeId") val employeeId: String?,
    @SerialName("employeeMiddleName") val employeeMiddleName: String?,
    @SerialName("employeeName") val employeeName: String?,
    @SerialName("employeePhone") val employeePhone: String?,
    @SerialName("employeeSurname") val employeeSurname: String?,
    @SerialName("photoUrl") val photoUrl: String?,
    @SerialName("previewPhotoUrl") val previewPhotoUrl: String?,
    @SerialName("lastActivity") val lastActivity: EmployeeLastActivityResponse?,
    @SerialName("employeeBotiqueAddress") val employeeBotiqueAddress: String?,
    @SerialName("employeeBotiqueAddressShort") val employeeBotiqueAddressShort: String?,
    @SerialName("employeeBrand") val employeeBrand: String?
)
