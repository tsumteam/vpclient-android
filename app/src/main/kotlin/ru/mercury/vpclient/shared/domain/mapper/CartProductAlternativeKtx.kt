package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.network.request.BasketOperationRequest
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType
import ru.mercury.vpclient.shared.data.network.response.BasketRemoveAlternativeOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.BasketSwitchProductWithAlternativeOperationRequestItemResponse

fun CartProductAlternative.removeAlternativeRequest(
    pairedUserId: String
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductAlternativeJson.encodeToJsonElement(
                BasketRemoveAlternativeOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.REMOVE_ALTERNATIVE,
                    operationOrder = 0,
                    alternativeId = id
                )
            )
        )
    )
}

fun CartProductAlternative.switchProductWithAlternativeRequest(
    pairedUserId: String
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            cartProductAlternativeJson.encodeToJsonElement(
                BasketSwitchProductWithAlternativeOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.SWITCH_PRODUCT_WITH_ALTERNATIVE,
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
