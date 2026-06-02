package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.network.entity.BasketAlternativesPaletteState
import ru.mercury.vpclient.shared.data.network.entity.BasketAddSameProductWithDifferentSizeToLineOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeAlternativePaletteStateOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineColorOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLinePaySwitchOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineQuantityOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineSizeOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestTypeEnum
import ru.mercury.vpclient.shared.data.network.entity.BasketRemoveLineOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketRemoveProductFromLineOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketSwitchAlternativeBackToOriginalOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.FittingChangeLinePaySwitchOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.FittingOperationRequestDto
import ru.mercury.vpclient.shared.data.network.entity.FittingOperationRequestTypeDto
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

fun CartProduct.fittingPaySwitchRequest(
    pairedUserId: String,
    paySwitch: Boolean
): FittingOperationRequestDto {
    return FittingOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                FittingChangeLinePaySwitchOperationRequestItemDto(
                    operationType = FittingOperationRequestTypeDto.CHANGE_LINE_PAY_SWITCH,
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
            ),
            cartProductJson.encodeToJsonElement(
                BasketChangeLinePaySwitchOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_PAY_SWITCH,
                    operationOrder = 1,
                    lineId = id,
                    paySwitch = true
                )
            )
        )
    )
}

fun CartProduct.changeColorRequest(
    pairedUserId: String,
    colorId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLineColorOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_COLOR,
                    operationOrder = 0,
                    lineId = id,
                    colorId = colorId
                )
            )
        )
    )
}

fun CartProduct.changeQuantityRequest(
    pairedUserId: String,
    quantity: Int
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLineQuantityOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_QUANTITY,
                    operationOrder = 0,
                    lineId = id,
                    quantity = quantity
                )
            )
        )
    )
}

fun CartProduct.addProductSizeRequest(
    pairedUserId: String,
    sizeId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketAddSameProductWithDifferentSizeToLineOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.ADD_SAME_PRODUCT_WITH_DIFFERENT_SIZE_TO_LINE,
                    operationOrder = 0,
                    lineId = id,
                    productId = sizeItems.firstOrNull()
                        ?.catalogProductId
                        ?.takeIf { it.isNotEmpty() }
                        ?: detailId,
                    sizeId = sizeId
                )
            )
        )
    )
}

fun CartProduct.removeProductSizeRequest(
    pairedUserId: String,
    size: CartProductSize
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketRemoveProductFromLineOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.REMOVE_PRODUCT_FROM_LINE,
                    operationOrder = 0,
                    lineId = id,
                    productId = size.productId
                )
            )
        )
    )
}

fun CartProduct.deleteProductRequest(
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketRemoveLineOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.REMOVE_LINE,
                    operationOrder = 0,
                    lineId = id
                )
            )
        )
    )
}

fun CartProduct.basketHideAlternativesRequest(
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeAlternativePaletteStateOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_ALTERNATIVE_PALETTE_STATE,
                    operationOrder = 0,
                    lineId = id,
                    paletteState = BasketAlternativesPaletteState.HIDDEN
                )
            )
        )
    )
}

fun CartProduct.basketShowAlternativesRequest(
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeAlternativePaletteStateOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_ALTERNATIVE_PALETTE_STATE,
                    operationOrder = 0,
                    lineId = id,
                    paletteState = BasketAlternativesPaletteState.OPEN
                )
            )
        )
    )
}

fun CartProduct.basketReturnOriginalRequest(
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketSwitchAlternativeBackToOriginalOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.SWITCH_ALTERNATIVE_BACK_TO_ORIGINAL,
                    operationOrder = 0,
                    lineId = id
                )
            )
        )
    )
}

private val cartProductJson = Json {
    explicitNulls = false
}
