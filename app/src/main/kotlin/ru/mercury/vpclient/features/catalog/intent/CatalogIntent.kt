package ru.mercury.vpclient.features.catalog.intent

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CatalogIntent: Intent {
    data object CollectCatalogData: CatalogIntent
    data object CollectCartCount: CatalogIntent
    data object CollectFittingCount: CatalogIntent
    data object CollectActiveEmployee: CatalogIntent
    data object LoadCatalogCategoriesTop: CatalogIntent
    data object FittingClick: CatalogIntent
    data object MessengerClick: CatalogIntent
    data object CartClick: CatalogIntent
    data class SelectTab(val tabIndex: Int): CatalogIntent
    data class CategoryClick(val entity: CatalogCategoryEntity): CatalogIntent
}
