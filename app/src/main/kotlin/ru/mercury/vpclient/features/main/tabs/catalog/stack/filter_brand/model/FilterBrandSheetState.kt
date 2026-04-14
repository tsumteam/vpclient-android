package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.model

import ru.mercury.vpclient.shared.entity.BrandFilterValue
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity

data class FilterBrandSheetState(
    val brands: List<BrandFilterValue>,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
)
