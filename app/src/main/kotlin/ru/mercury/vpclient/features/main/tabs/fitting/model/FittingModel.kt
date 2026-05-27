package ru.mercury.vpclient.features.main.tabs.fitting.model

import ru.mercury.vpclient.features.cart.model.CartPayMode
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.fittingText
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.domain.mapper.thousandsSeparator
import ru.mercury.vpclient.shared.mvi.Model
import kotlin.math.roundToInt

data class FittingModel(
    val cartSize: Int = 0,
    val cartBadge: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val products: List<CartProduct> = emptyList(),
    val apiFittingProducts: List<CartProduct> = emptyList(),
    val apiFittingDeliveries: List<FittingDeliveryData> = emptyList(),
    val payMode: CartPayMode = CartPayMode.All
): Model {

    val cartText: String
        get() = when {
            cartSize > 0 -> cartSize.toString()
            else -> ""
        }

    val showCartBadge: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = activeEmployee.fittingText

    val showFittingButton: Boolean
        get() = activeEmployee.hasFittingProducts

    val showFittingBadge: Boolean
        get() = activeEmployee.hasFittingBadge

    val showMessengerBadge: Boolean
        get() = activeEmployee.hasMessengerBadge

    val visibleProducts: List<CartProduct>
        get() {
            return when (payMode) {
                CartPayMode.All -> products
                CartPayMode.Payment -> paymentProducts
            }
        }

    val fittingProducts: List<CartProduct>
        get() {
            return apiFittingProducts
        }

    val visibleFittingProducts: List<CartProduct>
        get() {
            return when (payMode) {
                CartPayMode.All -> fittingProducts
                CartPayMode.Payment -> fittingPaymentProducts
            }
        }

    val allItemsCount: Int
        get() {
            return products.sumOf { it.itemsCount }
        }

    val paymentItemsCount: Int
        get() {
            return paymentProducts.sumOf { it.itemsCount }
        }

    val fittingProductsCount: Int
        get() {
            return fittingProducts.sumOf { it.itemsCount }
        }

    val fittingPaymentProductsCount: Int
        get() {
            return fittingPaymentProducts.sumOf { it.itemsCount }
        }

    val totalSummary: String
        get() {
            return summary(products)
        }

    val fittingSummary: String
        get() {
            return summary(fittingProducts)
        }

    val fittingPaymentSummary: String
        get() {
            return summary(fittingPaymentProducts)
        }

    private val paymentProducts: List<CartProduct>
        get() {
            return products.filter { it.isForPayment && !it.isSold }
        }

    private val fittingPaymentProducts: List<CartProduct>
        get() {
            return fittingProducts.filter { it.isForPayment }
        }

    private fun summary(products: List<CartProduct>): String {
        val itemsCount = products.sumOf { it.itemsCount }
        val sum = products.sumOf { (it.priceValue * it.itemsCount).roundToInt() }
        return "$itemsCount ${itemsCount.productsWord} на сумму ${FORMAT_RUB.format(sum.thousandsSeparator)}"
    }

    private val CartProduct.itemsCount: Int
        get() {
            return quantity * sizeCount
        }

    private val Int.productsWord: String
        get() {
            return when {
                this % 100 in 11..14 -> "товаров"
                this % 10 == 1 -> "товар"
                this % 10 in 2..4 -> "товара"
                else -> "товаров"
            }
        }
}
