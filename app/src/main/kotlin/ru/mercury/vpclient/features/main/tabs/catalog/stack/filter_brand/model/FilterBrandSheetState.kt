package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.model

import ru.mercury.vpclient.core.entity.BrandFilterValue
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity

data class FilterBrandSheetState(
    val brands: List<BrandFilterValue>,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
