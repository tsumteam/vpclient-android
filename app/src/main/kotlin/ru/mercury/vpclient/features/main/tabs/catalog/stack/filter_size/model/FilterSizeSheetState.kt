package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.model

import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity

data class FilterSizeSheetState(
    val entity: FilterValuesEntity,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
