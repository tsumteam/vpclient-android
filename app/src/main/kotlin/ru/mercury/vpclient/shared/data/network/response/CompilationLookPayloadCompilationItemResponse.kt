package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompilationLookPayloadCompilationItemResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null
)
