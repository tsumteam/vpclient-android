package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeFittingOrderAccessFlagsResponse(
    @SerialName("fittingHome") val fittingHome: Boolean? = null,
    @SerialName("fittingBtq") val fittingBtq: Boolean? = null,
    @SerialName("orderHome") val orderHome: Boolean? = null,
    @SerialName("orderBtq") val orderBtq: Boolean? = null
)
