package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsQuantityEntity

val CatalogFilterProductsQuantityEntity.isEmpty: Boolean
    get() = this == CatalogFilterProductsQuantityEntity.Empty

val CatalogFilterProductsQuantityEntity?.orEmpty: CatalogFilterProductsQuantityEntity
    get() = this ?: CatalogFilterProductsQuantityEntity.Empty

val CatalogFilterProductsQuantityEntity.requireProductsQuantity: Int
    get() = productsQuantity.orEmpty

val CatalogFilterProductsQuantityEntity.productsQuantityWithThousandsSeparator: String
    get() = requireProductsQuantity.thousandsSeparator
