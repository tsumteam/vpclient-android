package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model

import ru.mercury.vpclient.shared.data.entity.CatalogData
import ru.mercury.vpclient.shared.mvi.Model

data class CatalogModel(
    val catalogData: CatalogData = CatalogData(),
    val cartItemsCount: Int = 0,
    val cartBadge: Int = 0
): Model {

    val isLoading: Boolean
        get() = catalogData.pages.isEmpty()

    val cartText: String
        get() = when {
            cartItemsCount > 0 -> cartItemsCount.toString()
            else -> ""
        }

    val showCartBadge: Boolean
        get() = cartBadge > 0
}
