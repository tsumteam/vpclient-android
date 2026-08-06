package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.FittingCheckoutData
import ru.mercury.vpclient.shared.data.network.response.BasketForCheckoutResponse
import kotlin.math.roundToInt

val BasketForCheckoutResponse.entity: FittingCheckoutData
    get() {
        val paymentLines = basketResponseDto?.lines.orEmpty().filter { line -> line.paySwitch == true }
        val quantity = paymentLines.sumOf { line -> line.quantity.orEmpty }
        val orderAmount = paymentLines.sumOf { line ->
            val lineQuantity = line.quantity.orEmpty
            line.products.orEmpty().sumOf { basketProduct ->
                val product = basketProduct.product
                val price = product?.price ?: product?.currentRetailPrice.orEmpty
                (maxOf(product?.priceWithoutDiscount.orEmpty, price) * lineQuantity).roundToInt()
            }
        }
        val promotionDiscount = paymentLines.sumOf { line ->
            val lineQuantity = line.quantity.orEmpty
            line.products.orEmpty().sumOf { basketProduct ->
                val product = basketProduct.product
                val price = product?.price ?: product?.currentRetailPrice.orEmpty
                val priceWithoutDiscount = maxOf(product?.priceWithoutDiscount.orEmpty, price)
                ((priceWithoutDiscount - price) * lineQuantity).roundToInt()
            }
        }
        val totalBeforeBonuses = (orderAmount - promotionDiscount).coerceAtLeast(0)

        return FittingCheckoutData(
            deliveryIds = emptyList(),
            itemCount = quantity,
            orderAmount = orderAmount,
            promotionDiscount = promotionDiscount,
            availableBonusAmount = availableBonusSum.orEmpty
                .roundToInt()
                .coerceIn(0, totalBeforeBonuses),
            totalAvailableBonusAmount = totalAvailableBonuses.orEmpty.roundToInt().coerceAtLeast(0),
            loyaltyCardNumber = loyaltyCardNumber
        )
    }
