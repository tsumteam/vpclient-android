package ru.mercury.vpclient.features.catalog.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.domain.usecase.CatalogDataFlowUseCase.CatalogData
import ru.mercury.vpclient.shared.mvi.Model

data class CatalogModel(
    val catalogData: CatalogData = CatalogData(),
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val isLoading: Boolean
        get() = catalogData.pages.isEmpty()

    val selectedTabIndex: Int
        get() = catalogData.tabs.indexOfFirst { it.selected }.takeIf { it >= 0 } ?: 0

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
}
