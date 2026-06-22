package ru.mercury.vpclient.features.filter_brand_sheet.model

import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

data class FilterBrandModel(
    val brands: List<BrandFilterValue>,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
) {
    companion object {
        const val BRAND_ANIMATION_DURATION = 200
    }
}
