package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CheckUserResult

@Serializable
data class CheckUserResponse(
    @SerialName("result") val result: CheckUserResult? = null
)
