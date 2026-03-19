package ru.mercury.vpclient.core.entity

import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesEntity

data class FilterData(
    val filterTitleEntity: FilterTitleEntity,
    val filterRibbonData: FilterRibbonData,
    val quantityEntity: CatalogFilterProductsQuantityEntity,
    val filterValuesEntities: List<FilterValuesEntity> = emptyList()
) {
    companion object {
        val Empty = FilterData(
            filterTitleEntity = FilterTitleEntity.Empty,
            filterRibbonData = FilterRibbonData.Empty,
            quantityEntity = CatalogFilterProductsQuantityEntity.Empty
        )
    }
}
