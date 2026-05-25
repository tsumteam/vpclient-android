package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineLookOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineOrderOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestTypeEnum

fun cartProductsAfterDragRequest(
    products: List<CartProduct>,
    changedLookProducts: List<CartProduct>,
    pairedUserId: String
): BasketOperationRequestDto {
    var operationOrder = 0
    val items = mutableListOf<JsonElement>()

    changedLookProducts.forEach { product ->
        items.add(
            cartProductDragJson.encodeToJsonElement(
                BasketChangeLineLookOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_LOOK,
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
                BasketChangeLineOrderOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_ORDER,
                    operationOrder = operationOrder,
                    lineId = product.id,
                    order = index
                )
            )
        )
        operationOrder += 1
    }

    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = items
    )
}

private val cartProductDragJson = Json {
    explicitNulls = false
}
