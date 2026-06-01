package ru.mercury.vpclient.features.category.model

import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.shared.domain.mapper.fittingText
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge

data class CategoryModel(
    val entity: CatalogCategoryEntity = CatalogCategoryEntity.Empty,
    val pojos: List<SubcategoryPojo> = emptyList(),
    val cartSize: Int = 0,
    val cartBadge: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val cartText: String
        get() {
            return when {
                cartSize > 0 -> cartSize.toString()
                else -> ""
            }
        }

    val showCartBadge: Boolean
        get() {
            return cartBadge > 0
        }

    val fittingText: String
        get() = activeEmployee.fittingText

    val showFittingButton: Boolean
        get() = activeEmployee.hasFittingProducts

    val showFittingBadge: Boolean
        get() = activeEmployee.hasFittingBadge

    val showMessengerBadge: Boolean
        get() = activeEmployee.hasMessengerBadge
}
