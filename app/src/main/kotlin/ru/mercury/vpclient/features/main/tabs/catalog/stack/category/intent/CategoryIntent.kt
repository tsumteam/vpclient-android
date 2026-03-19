package ru.mercury.vpclient.features.main.tabs.catalog.stack.category.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface CategoryIntent: Intent {
    data object CollectCategoryEntity: CategoryIntent
    data object CollectCategoryPojos: CategoryIntent
    data object LoadCatalogCategoriesBottom: CategoryIntent
    data object BackClick: CategoryIntent
    data class FilterClick(val categoryId: Int, val titleCategoryId: Int, val subtitleCategoryId: Int): CategoryIntent
}
