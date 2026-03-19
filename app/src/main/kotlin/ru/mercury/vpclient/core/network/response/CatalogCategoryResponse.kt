package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogCategoryResponse(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("photoUrl") val photoUrl: String?,
    @SerialName("categoryType") val categoryType: String?,
    @SerialName("sortSettingId") val sortSettingId: Int?,
    @SerialName("children") val children: List<CatalogCategoryResponse>?
) {
    companion object {
        const val CATEGORY_TYPE_CATALOG = "catalog"
        const val CATEGORY_TYPE_GIFT_CARD = "giftCard"
        const val CATEGORY_TYPE_ACTION = "action"
    }
}
