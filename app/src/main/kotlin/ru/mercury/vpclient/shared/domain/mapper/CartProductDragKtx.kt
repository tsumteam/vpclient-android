package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLineLookOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLineOrderOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.request.BasketOperationRequest
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType

fun cartProductsAfterDragRequest(
    products: List<CartProduct>,
    changedLookProducts: List<CartProduct>,
    pairedUserId: String
): BasketOperationRequest {
    var operationOrder = 0
    val items = mutableListOf<JsonElement>()

    changedLookProducts.forEach { product ->
        items.add(
            cartProductDragJson.encodeToJsonElement(
                BasketChangeLineLookOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_LOOK,
                    operationOrder = operationOrder,
                    lineId = product.id,
                    lookId = product.lookId
                )
            )
        )
        operationOrder += 1
    }

    products.forEachIndexed { index, product ->
        items.add(
            cartProductDragJson.encodeToJsonElement(
                BasketChangeLineOrderOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_ORDER,
                    operationOrder = operationOrder,
                    lineId = product.id,
                    order = index
                )
            )
        )
        operationOrder += 1
    }

    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = items
    )
}

private val cartProductDragJson = Json {
    explicitNulls = false
}

fun List<CartProduct>.moveProductAfterDrag(
    productId: String,
    targetProductId: String,
    placeAfterTarget: Boolean
): List<CartProduct> {
    if (productId == targetProductId) {
        return this
    }

    val fromIndex = indexOfFirst { it.id == productId }
    val targetIndex = indexOfFirst { it.id == targetProductId }
    if (fromIndex == -1 || targetIndex == -1) {
        return this
    }

    val targetProduct = this[targetIndex]
    val product = this[fromIndex].copy(
        lookId = targetProduct.lookId,
        lookName = targetProduct.lookName,
        lookImageUrl = targetProduct.lookImageUrl
    )
    val products = toMutableList()
    products.removeAt(fromIndex)

    val actualTargetIndex = products.indexOfFirst { it.id == targetProductId }
    if (actualTargetIndex == -1) {
        return this
    }

    val insertIndex = when {
        placeAfterTarget -> actualTargetIndex + 1
        else -> actualTargetIndex
    }.coerceIn(0, products.size)
    products.add(insertIndex, product)

    return products
}
