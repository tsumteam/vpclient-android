package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompilationCreateRequest(
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null
)
