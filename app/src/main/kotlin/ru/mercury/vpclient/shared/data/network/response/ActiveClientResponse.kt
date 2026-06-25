package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActiveClientResponse(
    @SerialName("clientEmail") val clientEmail: String? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("clientMiddleName") val clientMiddleName: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("clientPhone") val clientPhone: String? = null,
    @SerialName("clientSurname") val clientSurname: String? = null,
    @SerialName("isAvailableFittingHome") val isAvailableFittingHome: Boolean? = null
)
