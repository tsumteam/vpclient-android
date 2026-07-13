package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity

data class BrandsPage(
    val tab: TabType,
    val catalogBrandEntities: List<CatalogBrandEntity> = emptyList(),
    val favoriteBrandEntities: List<CatalogBrandEntity> = emptyList(),
    val topBrandEntities: List<CatalogBrandEntity> = emptyList(),
    val sections: List<BrandsSection> = emptyList()
)
