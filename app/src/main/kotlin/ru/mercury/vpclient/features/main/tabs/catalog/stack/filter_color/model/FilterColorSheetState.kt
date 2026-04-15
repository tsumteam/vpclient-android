package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

data class FilterColorSheetState(
    val entity: FilterValuesEntity,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
