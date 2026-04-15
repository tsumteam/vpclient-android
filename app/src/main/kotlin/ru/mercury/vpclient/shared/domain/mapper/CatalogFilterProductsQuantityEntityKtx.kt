package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity

val CatalogFilterProductsQuantityEntity.isEmpty: Boolean
    get() = this == CatalogFilterProductsQuantityEntity.Empty

val CatalogFilterProductsQuantityEntity?.orEmpty: CatalogFilterProductsQuantityEntity
    get() = this ?: CatalogFilterProductsQuantityEntity.Empty

val CatalogFilterProductsQuantityEntity.requireProductsQuantity: Int
    get() = productsQuantity.orEmpty

val CatalogFilterProductsQuantityEntity.productsQuantityWithThousandsSeparator: String
    get() = requireProductsQuantity.thousandsSeparator
