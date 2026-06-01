package ru.mercury.vpclient.features.catalog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CatalogIntent: Intent {
    data object CollectCatalogScreenData: CatalogIntent
    data object CollectCartSize: CatalogIntent
    data object CollectActiveEmployee: CatalogIntent
    data object LoadEmployees: CatalogIntent
    data object LoadCartData: CatalogIntent
    data object LoadCatalogCategoriesTop: CatalogIntent
    data object FittingClick: CatalogIntent
    data object MessengerClick: CatalogIntent
    data object CartClick: CatalogIntent
    data class SelectTab(val tabIndex: Int): CatalogIntent
    data class CategoryClick(val categoryId: Int): CatalogIntent
}
