package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeFittingBrandLimitResponse(
    @SerialName("brand") val brand: String? = null,
    @SerialName("limitPerDay") val limitPerDay: Int? = null,
    @SerialName("currentValue") val currentValue: Int? = null
)
