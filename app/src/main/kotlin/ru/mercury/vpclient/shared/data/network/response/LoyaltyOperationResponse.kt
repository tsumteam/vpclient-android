package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.entity.ErrorDto

@Serializable
data class LoyaltyOperationResponse(
    @SerialName("error") val error: ErrorDto? = null
)
