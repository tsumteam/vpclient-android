package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.network.type.BasketAlternativesPaletteState
import ru.mercury.vpclient.shared.data.network.response.BasketAddSameProductWithDifferentSizeToLineOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketChangeAlternativePaletteStateOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLineColorOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLinePaySwitchOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLineQuantityOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLineSizeOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.request.BasketOperationRequest
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType
import ru.mercury.vpclient.shared.data.network.response.BasketRemoveLineOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketRemoveProductFromLineOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketSwitchAlternativeBackToOriginalOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.FittingChangeLinePaySwitchOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.request.FittingOperationRequest
import ru.mercury.vpclient.shared.data.network.type.FittingOperationRequestType
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLinePaySwitchOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_PAY_SWITCH,
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
): FittingOperationRequest {
    return FittingOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                FittingChangeLinePaySwitchOperationRequestItemResponse(
                    operationType = FittingOperationRequestType.CHANGE_LINE_PAY_SWITCH,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLineSizeOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_SIZE,
                    operationOrder = 0,
                    lineId = id,
                    sizeId = sizeId
                )
            ),
            cartProductJson.encodeToJsonElement(
                BasketChangeLinePaySwitchOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_PAY_SWITCH,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLineColorOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_COLOR,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeLineQuantityOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_QUANTITY,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketAddSameProductWithDifferentSizeToLineOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.ADD_SAME_PRODUCT_WITH_DIFFERENT_SIZE_TO_LINE,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketRemoveProductFromLineOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.REMOVE_PRODUCT_FROM_LINE,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketRemoveLineOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.REMOVE_LINE,
                    operationOrder = 0,
                    lineId = id
                )
            )
        )
    )
}

fun CartProduct.basketHideAlternativesRequest(
    pairedUserId: String
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeAlternativePaletteStateOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_ALTERNATIVE_PALETTE_STATE,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketChangeAlternativePaletteStateOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_ALTERNATIVE_PALETTE_STATE,
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
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductJson.encodeToJsonElement(
                BasketSwitchAlternativeBackToOriginalOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.SWITCH_ALTERNATIVE_BACK_TO_ORIGINAL,
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
