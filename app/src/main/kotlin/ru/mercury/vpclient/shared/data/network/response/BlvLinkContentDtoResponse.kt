package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.BlvLinkContentType

@Serializable
data class BlvLinkContentDtoResponse(
    @SerialName("orderNumber") val orderNumber: String? = null,
    @SerialName("products") val products: List<CatalogProductSearchCardVNResponse>? = null,
    @SerialName("type") val type: BlvLinkContentType? = null
)
