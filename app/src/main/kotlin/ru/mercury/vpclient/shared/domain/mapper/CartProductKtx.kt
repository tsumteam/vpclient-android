package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLinePaySwitchOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineSizeOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestTypeEnum
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity

val CartProduct.imagePages: List<String>
    get() {
        val pages = imageUrls.filter { it.isNotEmpty() }

        return when {
            pages.isNotEmpty() -> pages
            imageUrl.isNotEmpty() -> listOf(imageUrl)
            else -> listOf("")
        }
    }

fun CartProduct.catalogFilterProductsEntity(position: Int): CatalogFilterProductsEntity? {
    return when {
        detailId.isEmpty() -> null
        itemId.isEmpty() -> null
        colorId.isEmpty() -> null
        else -> {
            CatalogFilterProductsEntity(
                categoryId = 0,
                titleCategoryId = 0,
                id = detailId,
                itemId = itemId,
                colorId = colorId,
                name = name,
                price = priceValue,
                priceWithoutDiscount = null,
                brand = brand,
                urlBrandLogo = urlBrandLogo,
                imageUrl = imageUrl,
                imageUrls = imageUrls,
                additionalColorPhotoUrls = emptyList(),
                position = position
            )
        }
    }
}

fun CartProduct.paySwitchRequest(
    pairedUserId: String,
    paySwitch: Boolean
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLinePaySwitchOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_PAY_SWITCH,
                    operationOrder = 0,
                    lineId = id,
                    paySwitch = paySwitch
                )
            )
        )
    )
}

fun CartProduct.changeSizeRequest(
    pairedUserId: String,
    sizeId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLineSizeOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_SIZE,
                    operationOrder = 0,
                    lineId = id,
                    sizeId = sizeId
                )
            )
        )
    )
}

private val cartProductJson = Json {
    explicitNulls = false
}
