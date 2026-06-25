package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportCompilationStatisticsResponseItemResponse(
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("employeeName") val employeeName: String? = null,
    @SerialName("areaId") val areaId: String? = null,
    @SerialName("brandId") val brandId: String? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("crmId") val crmId: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("clientPhone") val clientPhone: String? = null,
    @SerialName("clientRegistrationDate") val clientRegistrationDate: String? = null,
    @SerialName("compilationName") val compilationName: String? = null,
    @SerialName("compilationId") val compilationId: Int? = null,
    @SerialName("compilationCreateDate") val compilationCreateDate: String? = null,
    @SerialName("compilationDescription") val compilationDescription: String? = null,
    @SerialName("isOpenCompilation") val isOpenCompilation: Boolean? = null,
    @SerialName("isSentCompilation") val isSentCompilation: Boolean? = null,
    @SerialName("isActiveCompilation") val isActiveCompilation: Boolean? = null,
    @SerialName("isArchivedCompilation") val isArchivedCompilation: Boolean? = null,
    @SerialName("clientCompilationCreateDate") val clientCompilationCreateDate: String? = null,
    @SerialName("clientCompilationOpeningDate") val clientCompilationOpeningDate: String? = null,
    @SerialName("lookName") val lookName: String? = null,
    @SerialName("lookId") val lookId: Int? = null,
    @SerialName("lookPhotoUrl") val lookPhotoUrl: String? = null,
    @SerialName("lookCreateDate") val lookCreateDate: String? = null,
    @SerialName("lookSource") val lookSource: CompilationLookSourceResponse? = null,
    @SerialName("itemBrand") val itemBrand: String? = null,
    @SerialName("itemKttId") val itemKttId: String? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("itemColorId") val itemColorId: String? = null,
    @SerialName("itemIsViewed") val itemIsViewed: Boolean? = null,
    @SerialName("itemIsAddedToBasket") val itemIsAddedToBasket: Boolean? = null,
    @SerialName("itemIsAddedToFitting") val itemIsAddedToFitting: Boolean? = null,
    @SerialName("price") val price: Double? = null
)
