package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.response.BasketChangeLineLookOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.request.BasketOperationRequest
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType
import ru.mercury.vpclient.shared.data.network.response.BasketRemoveLookOperationRequestItemResponse

fun deleteLookRequest(
    lookId: String,
    pairedUserId: String
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartLookJson.encodeToJsonElement(
                BasketRemoveLookOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.REMOVE_LOOK,
                    operationOrder = 0,
                    lookId = lookId
                )
            )
        )
    )
}

fun disassembleLookRequest(
    products: List<CartProduct>,
    pairedUserId: String
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = products.mapIndexed { index, product ->
            cartLookJson.encodeToJsonElement(
                BasketChangeLineLookOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.CHANGE_LINE_LOOK,
                    operationOrder = index,
                    lineId = product.id,
                    lookId = null
                )
            )
        }
    )
}

private val cartLookJson = Json {
    explicitNulls = false
}
