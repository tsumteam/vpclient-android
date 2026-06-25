package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ru.mercury.vpclient.shared.data.network.response.CompilationLookSaveProductResponse

@Serializable
data class CompilationLookSaveRequest(
    @SerialName("compilationId") val compilationId: Int? = null,
    @SerialName("meta") val meta: JsonElement? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("products") val products: List<CompilationLookSaveProductResponse>? = null
)
