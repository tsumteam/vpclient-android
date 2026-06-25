package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.Gender

@Serializable
data class GenderDtoResponse(
    @SerialName("gender") val gender: Gender? = null
)
