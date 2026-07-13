package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity

data class BrandsSection(
    val letter: String,
    val catalogBrandEntities: List<CatalogBrandEntity>
)
