package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ClientClass

@Serializable
data class ReserveResponse(
    @SerialName("reserveId") val reserveId: String? = null,
    @SerialName("reserveReason") val reserveReason: String? = null,
    @SerialName("reserveStartDate") val reserveStartDate: String? = null,
    @SerialName("reserveEndDate") val reserveEndDate: String? = null,
    @SerialName("employee") val employee: String? = null,
    @SerialName("employeePhone") val employeePhone: String? = null,
    @SerialName("fittingType") val fittingType: String? = null,
    @SerialName("clientType") val clientType: ClientClass? = null
)
