package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CatalogItemType

@Serializable
data class LookItemResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("orderBy") val orderBy: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("category") val category: CatalogItemType? = null
)
