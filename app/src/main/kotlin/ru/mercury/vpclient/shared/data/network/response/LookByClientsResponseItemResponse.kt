package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookByClientsResponseItemResponse(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("surName") val surName: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("lastActivityDate") val lastActivityDate: String? = null,
    @SerialName("statistics") val statistics: StatisticsResponseBaseResponse? = null
)
