package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUserResponse(
    @SerialName("clientEmail") val clientEmail: String? = null,
    @SerialName("clientMiddleName") val clientMiddleName: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("clientPhone") val clientPhone: String? = null,
    @SerialName("clientSurname") val clientSurname: String? = null,
    @SerialName("isAvailableFittingHome") val isAvailableFittingHome: Boolean? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("createDate") val createDate: String? = null,
    @SerialName("deviceId") val deviceId: String? = null,
    @SerialName("disableCommunications") val disableCommunications: Boolean? = null,
    @SerialName("employeeBotiqueAddress") val employeeBotiqueAddress: String? = null,
    @SerialName("employeeBotiqueAddressShort") val employeeBotiqueAddressShort: String? = null,
    @SerialName("employeeBrand") val employeeBrand: String? = null,
    @SerialName("employeeBrandWithPrefix") val employeeBrandWithPrefix: String? = null,
    @SerialName("employeeDomainAcc") val employeeDomainAcc: String? = null,
    @SerialName("employeeEmail") val employeeEmail: String? = null,
    @SerialName("employeeMiddleName") val employeeMiddleName: String? = null,
    @SerialName("employeeName") val employeeName: String? = null,
    @SerialName("employeePhone") val employeePhone: String? = null,
    @SerialName("employeeSurname") val employeeSurname: String? = null,
    @SerialName("gender") val gender: String? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("isBoutique") val isBoutique: Boolean? = null,
    @SerialName("isEmployee") val isEmployee: Boolean? = null,
    @SerialName("isRegistered") val isRegistered: Boolean? = null,
    @SerialName("employeeMaxProductsAddedToFittingPerDay") val employeeMaxProductsAddedToFittingPerDay: Double? = null,
    @SerialName("employeePhotoUrl") val employeePhotoUrl: String? = null,
    @SerialName("employeePreviewPhotoUrl") val employeePreviewPhotoUrl: String? = null,
    @SerialName("employeeCurrentProductsAddedToFittingPerDay") val employeeCurrentProductsAddedToFittingPerDay: Double? = null,
    @SerialName("employeeBuyoutPercent") val employeeBuyoutPercent: Double? = null,
    @SerialName("useDiginetica") val useDiginetica: Boolean? = null,
    @SerialName("hrId") val hrId: Int? = null
) {
    companion object {
        const val GENDER_NONE = "none"
        const val GENDER_MASCULINE = "masculine"
        const val GENDER_FEMININE = "feminine"
    }
}
