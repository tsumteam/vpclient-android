package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.model

import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity

data class FilterValuesSheetState(
    val entity: FilterValuesEntity,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
