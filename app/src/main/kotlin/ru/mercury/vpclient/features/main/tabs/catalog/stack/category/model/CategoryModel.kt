package ru.mercury.vpclient.features.main.tabs.catalog.stack.category.model

import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo

data class CategoryModel(
    val entity: CatalogCategoryEntity = CatalogCategoryEntity.Empty,
    val pojos: List<SubcategoryPojo> = emptyList(),
    val cartItemsCount: Int = 0,
    val cartBadge: Int = 0
): Model {

    val cartText: String
        get() {
            return when {
                cartItemsCount > 0 -> cartItemsCount.toString()
                else -> ""
            }
        }

    val showCartBadge: Boolean
        get() {
            return cartBadge > 0
        }
}
