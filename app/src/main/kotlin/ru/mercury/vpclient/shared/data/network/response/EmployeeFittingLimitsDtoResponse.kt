package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeFittingLimitsDtoResponse(
    @SerialName("isLimited") val isLimited: Boolean? = null,
    @SerialName("isLimitExceeded") val isLimitExceeded: Boolean? = null,
    @SerialName("limitPerDay") val limitPerDay: Int? = null,
    @SerialName("currentValue") val currentValue: Int? = null,
    @SerialName("brandLimits") val brandLimits: List<EmployeeFittingBrandLimitResponse>? = null
)
