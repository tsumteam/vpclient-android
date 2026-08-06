package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.FittingCheckoutData
import ru.mercury.vpclient.shared.data.network.response.FittingForCheckoutResponse
import kotlin.math.roundToInt

val FittingForCheckoutResponse.entity: FittingCheckoutData
    get() {
        val paymentDeliveries = fittingResponseDto?.deliveries.orEmpty()
            .map { delivery ->
                delivery to delivery.lines.orEmpty().filter { line -> line.paySwitch == true }
            }
            .filter { (_, paymentLines) -> paymentLines.isNotEmpty() }
        val paymentLines = paymentDeliveries.flatMap { (_, lines) -> lines }
        val orderAmount = paymentLines.sumOf { line ->
            val product = line.product
            val price = product?.price ?: product?.currentRetailPrice.orEmpty
            maxOf(product?.priceWithoutDiscount.orEmpty, price).roundToInt()
        }
        val promotionDiscount = paymentLines.sumOf { line ->
            val product = line.product
            val price = product?.price ?: product?.currentRetailPrice.orEmpty
            val priceWithoutDiscount = maxOf(product?.priceWithoutDiscount.orEmpty, price)
            ((priceWithoutDiscount - price) * product?.quantity.orEmpty.coerceAtLeast(1)).roundToInt()
        }
        val totalBeforeBonuses = (orderAmount - promotionDiscount).coerceAtLeast(0)

        return FittingCheckoutData(
            deliveryIds = paymentDeliveries
                .map { (delivery, _) -> delivery.deliveryId.orEmpty() }
                .filter(String::isNotBlank),
            itemCount = paymentLines.size,
            orderAmount = orderAmount,
            promotionDiscount = promotionDiscount,
            availableBonusAmount = availableBonusSum.orEmpty
                .roundToInt()
                .coerceIn(0, totalBeforeBonuses),
            totalAvailableBonusAmount = totalAvailableBonuses.orEmpty.roundToInt().coerceAtLeast(0),
            loyaltyCardNumber = loyaltyCardNumber
        )
    }
