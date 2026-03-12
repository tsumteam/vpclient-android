package ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface SubcategoryIntent: Intent {
    data object CollectSubcategoryEntity: SubcategoryIntent
    data object CollectSubcategoryPojos: SubcategoryIntent
    data object LoadCatalogCategoriesBottom: SubcategoryIntent
    data object BackClick: SubcategoryIntent
}
