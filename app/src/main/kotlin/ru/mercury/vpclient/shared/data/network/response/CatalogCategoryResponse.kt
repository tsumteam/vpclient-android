package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CatalogCategoryType

@Serializable
data class CatalogCategoryResponse(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("photoUrl") val photoUrl: String?,
    @SerialName("categoryType") val categoryType: CatalogCategoryType?,
    @SerialName("sortSettingId") val sortSettingId: Int?,
    @SerialName("children") val children: List<CatalogCategoryResponse>?
)
