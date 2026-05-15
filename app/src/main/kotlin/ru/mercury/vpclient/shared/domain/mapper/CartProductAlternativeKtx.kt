package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestDto
import ru.mercury.vpclient.shared.data.network.entity.BasketOperationRequestTypeEnum
import ru.mercury.vpclient.shared.data.network.entity.BasketRemoveAlternativeOperationRequestItemDto
import ru.mercury.vpclient.shared.data.network.entity.BasketSwitchProductWithAlternativeOperationRequestItemDto

fun CartProductAlternative.removeAlternativeRequest(
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductAlternativeJson.encodeToJsonElement(
                BasketRemoveAlternativeOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.REMOVE_ALTERNATIVE,
                    operationOrder = 0,
                    alternativeId = id
                )
            )
        )
    )
}

fun CartProductAlternative.switchProductWithAlternativeRequest(
    pairedUserId: String
): BasketOperationRequestDto {
    return BasketOperationRequestDto(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductAlternativeJson.encodeToJsonElement(
                BasketSwitchProductWithAlternativeOperationRequestItemDto(
                    operationType = BasketOperationRequestTypeEnum.SWITCH_PRODUCT_WITH_ALTERNATIVE,
                    operationOrder = 0,
                    alternativeId = id
                )
            )
        )
    )
}

private val cartProductAlternativeJson = Json {
    explicitNulls = false
}
