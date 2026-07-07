package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardResponse
import ru.mercury.vpclient.shared.data.network.response.LookActionInfoResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity

fun CatalogProductSearchCardResponse.toCatalogFilterProductsEntity(
    position: Int,
    categoryId: Int = 0,
    titleCategoryId: Int = 0,
    lookAction: LookActionInfoResponse? = null
): CatalogFilterProductsEntity {
    return CatalogFilterProductsEntity(
        categoryId = categoryId,
        titleCategoryId = titleCategoryId,
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
        position = position,
        lookActionPrice = lookAction?.price,
        lookActionPriceWithoutDiscount = lookAction?.priceWithoutDiscount,
        lookActionName = lookAction?.actionName,
        lookActionDiscountPercentage = lookAction?.discountPercentage,
        availableSizes = sizes?.let { sizes ->
            ProductAvailableSizesEntity(
                items = sizes.mapNotNull { size ->
                    val sizeId = size.id ?: return@mapNotNull null
                    ProductAvailableSizeEntity(
                        sizeId = sizeId,
                        russianSize = size.sizeForFilter,
                        sizeFullName = size.name,
                        inStock = size.inStock == true
                    )
                },
                countryCode = null,
                sizeTableTitle = null,
                sizeTableUrl = null
            )
        },
        isOneSize = oneSize == true
    )
}
