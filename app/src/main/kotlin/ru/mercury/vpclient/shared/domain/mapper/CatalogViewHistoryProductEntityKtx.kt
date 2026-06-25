package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogViewHistoryProductEntity

fun CatalogProductSearchCardResponse.entity(
    position: Int
): CatalogViewHistoryProductEntity {
    return CatalogViewHistoryProductEntity(
        id = id.orEmpty(),
        itemId = itemId.orEmpty(),
        colorId = colorId.orEmpty(),
        name = name.orEmpty(),
        price = price.orEmpty,
        priceWithoutDiscount = priceWithoutDiscount,
        brand = brand.orEmpty(),
        urlBrandLogo = urlBrandLogo,
        imageUrl = imageUrl.orEmpty(),
        imageUrls = imageUrls.orEmpty(),
        additionalColorPhotoUrls = additionalColors.orEmpty().mapNotNull { it.photoUrl },
        actionLabels = actions.orEmpty()
            .filter { action -> action.isCashDesk == true }
            .mapNotNull { action -> action.name },
        position = position
    )
}
