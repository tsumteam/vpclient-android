package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CatalogIntent: Intent {
    data object CollectCatalogScreenData: CatalogIntent
    data object LoadCatalogCategoriesTop: CatalogIntent
    data class SelectTab(val tabIndex: Int): CatalogIntent
    data class CategoryClick(val categoryId: Int): CatalogIntent
}
