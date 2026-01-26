package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.response.OrderLineResponse
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

fun OrderLineResponse.productEntity(
    currentProductEntity: ProductEntity
): ProductEntity {
    return currentProductEntity.copy(
        status = currentProductEntity.status,
        price = price.orEmpty,
        priceWithDisc = priceWithDisc.orEmpty,
        quantity = qty.orEmpty,
        nameAlias = nameAlias.orEmpty(),
        submarkName = submarkName.orEmpty()
    )
}
