package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CurrentUserResponse(
    val clientEmail: String? = null,
    val clientMiddleName: String? = null,
    val clientName: String? = null,
    val clientPhone: String? = null,
    val clientSurname: String? = null,
    val isAvailableFittingHome: Boolean? = null,
    val code: String? = null,
    val createDate: String? = null,
    val deviceId: String? = null,
    val disableCommunications: Boolean? = null,
    val employeeBotiqueAddress: String? = null,
    val employeeBotiqueAddressShort: String? = null,
    val employeeBrand: String? = null,
    val employeeBrandWithPrefix: String? = null,
    val employeeDomainAcc: String? = null,
    val employeeEmail: String? = null,
    val employeeMiddleName: String? = null,
    val employeeName: String? = null,
    val employeePhone: String? = null,
    val employeeSurname: String? = null,
    val gender: String? = null,
    val id: Int? = null,
    val isBoutique: Boolean? = null,
    val isEmployee: Boolean? = null,
    val isRegistered: Boolean? = null,
    val employeeMaxProductsAddedToFittingPerDay: Double? = null,
    val employeePhotoUrl: String? = null,
    val employeePreviewPhotoUrl: String? = null,
    val employeeCurrentProductsAddedToFittingPerDay: Double? = null,
    val employeeBuyoutPercent: Double? = null,
    val useDiginetica: Boolean? = null,
    val hrId: Int? = null
) {
    companion object {
        const val GENDER_NONE = "none"
        const val GENDER_MASCULINE = "masculine"
        const val GENDER_FEMININE = "feminine"
    }
}
