package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsByClientsResponse(
    @SerialName("sentQty") val sentQty: Int? = null,
    @SerialName("activeQty") val activeQty: Int? = null,
    @SerialName("openedQty") val openedQty: Int? = null
)
