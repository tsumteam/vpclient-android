package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.RouteId
import ru.mercury.vpclient.core.network.response.ProductResponse
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

fun ProductResponse.entity(
    routeId: RouteId,
    boutiqueId: BoutiqueId,
    deliveryId: DeliveryId,
    position: Int
): ProductEntity {
    return ProductEntity(
        routeId = routeId,
        boutiqueId = boutiqueId,
        deliveryId = deliveryId,
        cis = cis.orEmpty(),
        barcode = barcode.orEmpty(),
        itemColorId = itemColorId.orEmpty(),
        itemId = itemId.orEmpty(),
        itemName = itemName.orEmpty(),
        itemSizeId = itemSizeId.orEmpty(),
        lineId = lineId.orEmpty().ifEmpty(itemId::orEmpty),
        price = price.orEmpty,
        priceWithDisc = priceWithDisc.orEmpty,
        quantity = quantity.orEmpty,
        status = status.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        nameAlias = "",
        submarkName = "",
        position = position,
        isToPay = false
    )
}

fun ProductResponse.entity(
    currentProductEntity: ProductEntity
): ProductEntity {
    return ProductEntity(
        routeId = currentProductEntity.routeId,
        boutiqueId = currentProductEntity.boutiqueId,
        deliveryId = currentProductEntity.deliveryId,
        cis = cis.orEmpty(),
        barcode = barcode.orEmpty(),
        itemColorId = itemColorId.orEmpty(),
        itemId = itemId.orEmpty(),
        itemName = itemName.orEmpty(),
        itemSizeId = itemSizeId.orEmpty(),
        lineId = lineId.orEmpty().ifEmpty(itemId::orEmpty),
        price = price.orEmpty,
        priceWithDisc = priceWithDisc.orEmpty,
        quantity = quantity.orEmpty,
        status = status.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        nameAlias = currentProductEntity.nameAlias,
        submarkName = currentProductEntity.submarkName,
        position = currentProductEntity.position,
        isToPay = currentProductEntity.isToPay
    )
}
