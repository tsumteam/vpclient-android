package ru.mercury.vpclient.features.profile_view_history.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileViewHistoryModel(
    val basketProductIds: Set<String> = emptySet(),
    val basketProductKeys: Set<String> = emptySet(),
    val cartCount: Int = 0,
    val cartBadge: Int = 0
): Model {

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    fun isProductInBasket(entity: CatalogFilterProductsEntity): Boolean {
        return "${entity.itemId}:${entity.colorId}:" in basketProductKeys ||
            "${entity.itemId}:${entity.colorId}:NS" in basketProductKeys
    }
}
