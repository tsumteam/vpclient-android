package ru.mercury.vpclient.features.filter_tree_sheet.model

import ru.mercury.vpclient.shared.data.entity.FilterTreeValue
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

data class FilterTreeModel(
    val title: String,
    val currentParentId: String?,
    val currentParentLabel: String?,
    val values: List<FilterTreeValue>,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
