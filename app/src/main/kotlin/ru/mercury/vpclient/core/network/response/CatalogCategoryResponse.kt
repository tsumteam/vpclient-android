package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogCategoryResponse(
    val id: Int?,
    val name: String?,
    val photoUrl: String?,
    val categoryType: String?,
    val sortSettingId: Int?,
    val children: List<CatalogCategoryResponse>?
) {
    companion object {
        const val CATEGORY_TYPE_CATALOG = "catalog"
        const val CATEGORY_TYPE_GIFT_CARD = "giftCard"
        const val CATEGORY_TYPE_ACTION = "action"
    }
}
