package ru.mercury.vpclient.features.category.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

data class CategoryModel(
    val catalogCategoryEntity: CatalogCategoryEntity = CatalogCategoryEntity.Empty,
    val subcategoryPojos: List<SubcategoryPojo> = emptyList(),
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge

    val isViewAllButtonVisible: Boolean
        get() = "подар" !in catalogCategoryEntity.name.lowercase()

    val viewAllButtonTitleRes: Int
        get() = when (catalogCategoryEntity.id) {
            in SHOE_CATEGORY_IDS -> ClientStrings.CatalogViewAllShoes
            in CLOTHING_CATEGORY_IDS -> ClientStrings.CatalogViewAllClothing
            in BAG_CATEGORY_IDS -> ClientStrings.CatalogViewAllBags
            in ACCESSORY_CATEGORY_IDS -> ClientStrings.CatalogViewAllAccessories
            in BABY_CATEGORY_IDS -> ClientStrings.CatalogViewAllBaby
            else -> ClientStrings.CatalogViewAll
        }

    private companion object {
        private val SHOE_CATEGORY_IDS = setOf(154, 423, 764, 818)
        private val CLOTHING_CATEGORY_IDS = setOf(3, 317, 680, 574)
        private val BAG_CATEGORY_IDS = setOf(202, 469)
        private val ACCESSORY_CATEGORY_IDS = setOf(236, 495, 926)
        private val BABY_CATEGORY_IDS = setOf(867)
    }
}
