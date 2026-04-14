package ru.mercury.vpclient.shared.entity

import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity

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
