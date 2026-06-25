package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CatalogCategoryType

@Serializable
data class CatalogCategoryDtoResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("categoryType") val categoryType: CatalogCategoryType? = null,
    @SerialName("children") val children: List<CatalogCategoryDtoResponse>? = null
)
