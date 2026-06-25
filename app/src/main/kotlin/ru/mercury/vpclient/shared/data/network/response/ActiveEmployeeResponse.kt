package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActiveEmployeeResponse(
    @SerialName("employeeEmail") val employeeEmail: String? = null,
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("employeeMiddleName") val employeeMiddleName: String? = null,
    @SerialName("employeeName") val employeeName: String? = null,
    @SerialName("employeePhone") val employeePhone: String? = null,
    @SerialName("employeeSurname") val employeeSurname: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("previewPhotoUrl") val previewPhotoUrl: String? = null,
    @SerialName("employeeBotiqueAddress") val employeeBotiqueAddress: String? = null,
    @SerialName("employeeBotiqueAddressShort") val employeeBotiqueAddressShort: String? = null,
    @SerialName("employeeBrand") val employeeBrand: String? = null
)
