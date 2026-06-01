package ru.mercury.vpclient.features.filter_price_sheet.model

import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

data class FilterPriceModel(
    val title: String,
    val presets: List<FilterChip>,
    val selectedPresetId: String?,
    val priceFrom: String,
    val priceTo: String,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
