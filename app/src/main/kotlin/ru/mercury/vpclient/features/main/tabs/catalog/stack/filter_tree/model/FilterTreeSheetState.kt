package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_tree.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

data class FilterTreeSheetState(
    val title: String,
    val currentParentId: String?,
    val currentParentLabel: String?,
    val values: List<FilterTreeValue>,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)

data class FilterTreeValue(
    val id: String,
    val label: String,
    val hasChildren: Boolean
)
