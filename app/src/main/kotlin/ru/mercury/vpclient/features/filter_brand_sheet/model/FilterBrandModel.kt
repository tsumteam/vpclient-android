package ru.mercury.vpclient.features.filter_brand_sheet.model

import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.mvi.Model

data class FilterBrandModel(
    val brands: List<BrandFilterValue>,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
): Model {

    companion object {
        const val BRAND_ANIMATION_DURATION = 200
    }
}
