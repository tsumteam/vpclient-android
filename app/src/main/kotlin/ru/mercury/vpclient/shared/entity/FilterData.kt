package ru.mercury.vpclient.shared.entity

import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity

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
