package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.AvailableMultipleSizesResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity

fun AvailableMultipleSizesResponse.toProductAvailableSizesEntity(): ProductAvailableSizesEntity {
    return ProductAvailableSizesEntity(
        items = items.orEmpty().mapNotNull { size ->
            val sizeId = size.sizeId ?: return@mapNotNull null
            ProductAvailableSizeEntity(
                sizeId = sizeId,
                russianSize = size.russianSize,
                sizeFullName = size.sizeFullName,
                inStock = size.inStock == true
            )
        },
        countryCode = countryCode,
        sizeTableTitle = sizeTableTitle,
        sizeTableUrl = sizeTableUrl
    )
}
