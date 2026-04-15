package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity

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
