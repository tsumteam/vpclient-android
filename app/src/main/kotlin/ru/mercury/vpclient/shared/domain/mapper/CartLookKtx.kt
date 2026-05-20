package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.entity.BasketChangeLineLookOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestTypeEnum
import ru.mercury.vpclient.shared.data.network.entity.BasketRemoveLookOperationRequestItemDto

fun deleteLookRequest(
    lookId: String,
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartLookJson.encodeToJsonElement(
                BasketRemoveLookOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.REMOVE_LOOK,
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
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = products.mapIndexed { index, product ->
            cartLookJson.encodeToJsonElement(
                BasketChangeLineLookOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.CHANGE_LINE_LOOK,
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
