package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ActionItemType
import ru.mercury.vpclient.shared.data.network.type.CatalogItemType

@Serializable
data class ActionItemDtoResponse(
    @SerialName("dateFrom") val dateFrom: String? = null,
    @SerialName("dateTo") val dateTo: String? = null,
    @SerialName("fashionImageId") val fashionImageId: Int? = null,
    @SerialName("fashionImageItemsQty") val fashionImageItemsQty: Int? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("orderBy") val orderBy: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("category") val category: CatalogItemType? = null,
    @SerialName("type") val type: ActionItemType? = null
)
