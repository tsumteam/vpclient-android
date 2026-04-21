package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_price.model

import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

data class FilterPriceSheetState(
    val title: String,
    val presets: List<FilterChip>,
    val selectedPresetId: String?,
    val priceFrom: String,
    val priceTo: String,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
