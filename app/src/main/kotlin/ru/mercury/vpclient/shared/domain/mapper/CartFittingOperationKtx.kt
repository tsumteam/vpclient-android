package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.response.FittingDeliveryResponse

val fittingOperationJson = Json {
    explicitNulls = false
}

suspend fun NetworkService.fittingDeliveryForProduct(
    pairedUserId: String,
    product: CartProduct
): FittingDeliveryResponse? {
    return handleResponseResult {
        fittingsByPairedUserId(pairedUserId)
    }.getOrNull()
        ?.deliveries
        .orEmpty()
        .firstOrNull { delivery ->
            delivery.lines.orEmpty().any { line ->
                line.lineId == product.id
            }
        }
}
