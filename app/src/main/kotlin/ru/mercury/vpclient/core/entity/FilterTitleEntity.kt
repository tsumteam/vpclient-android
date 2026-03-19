package ru.mercury.vpclient.core.entity

import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity

data class FilterTitleEntity(
    val titleCatalogCategoryEntity: CatalogCategoryEntity,
    val subtitleCatalogCategoryEntity: CatalogCategoryEntity
) {
    companion object {
        val Empty = FilterTitleEntity(
            titleCatalogCategoryEntity = CatalogCategoryEntity.Empty,
            subtitleCatalogCategoryEntity = CatalogCategoryEntity.Empty
        )
    }
}
