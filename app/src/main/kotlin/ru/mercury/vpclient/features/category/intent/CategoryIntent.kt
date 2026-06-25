package ru.mercury.vpclient.features.category.intent

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CategoryIntent: Intent {
    data object CollectCategoryEntity: CategoryIntent
    data object CollectSubcategoryPojos: CategoryIntent
    data object CollectCartCount: CategoryIntent
    data object CollectFittingCount: CategoryIntent
    data object CollectActiveEmployee: CategoryIntent
    data object LoadCatalogCategoriesBottom: CategoryIntent
    data object BackClick: CategoryIntent
    data object CartClick: CategoryIntent
    data object FittingClick: CategoryIntent
    data object MessengerClick: CategoryIntent
    data class FilterClick(val entity: CatalogCategoryEntity): CategoryIntent
}
