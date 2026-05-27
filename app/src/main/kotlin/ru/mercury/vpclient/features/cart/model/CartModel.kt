package ru.mercury.vpclient.features.cart.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.mapper.thousandsSeparator
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.components.cart.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import kotlin.math.roundToInt

private const val DEFAULT_CART_CONSULTANT_NAME = "Персональный менеджер"
private const val DEFAULT_FITTING_SHEET_CLIENT_NAME = "Клиент"

data class CartModel(
    val payMode: CartPayMode = CartPayMode.All,
    val viewMode: CartViewMode = CartViewMode.List,
    val isRefreshing: Boolean = false,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val products: List<CartProduct> = emptyList(),
    val apiFittingProducts: List<CartProduct> = emptyList(),
    val apiFittingDeliveries: List<FittingDeliveryData> = emptyList(),
    val editProduct: CartProduct? = null,
    val selectSizeProduct: CartProduct? = null,
    val isFittingSheetVisible: Boolean = false,
    val isFittingProductsSheetVisible: Boolean = false,
    val sizePickerProduct: CartProduct? = null,
    val sizePickerSizes: ProductAvailableSizesEntity? = null,
    val sizePickerSelectedId: String? = null,
    val selectedAlternativeId: String? = null,
    val paySwitchJob: Job? = null,
    val sizePickerJob: Job? = null,
    val fittingSheetClientName: String = DEFAULT_FITTING_SHEET_CLIENT_NAME,
    val isFittingSheetClientFeminine: Boolean = false
): Model {
    val sizePickerState: SizeSelectorState
        get() {
            val sizes = sizePickerSizes ?: return SizeSelectorState.Empty
            return SizeSelectorState(
                sizes = sizes.items.map { size ->
                    SizeState(
                        topText = size.sizeId.uppercase(),
                        bottomText = size.russianSize ?: "-",
                        selected = size.sizeId == sizePickerSelectedId,
                        enabled = size.inStock
                    )
                },
                topText = sizes.countryCode.orEmpty(),
                bottomText = "RU",
                isSizeTableVisible = false
            )
        }

    val visibleProducts: List<CartProduct>
        get() {
            return when (payMode) {
                CartPayMode.All -> products
                CartPayMode.Payment -> paymentProducts
            }
        }

    val visibleProductGroups: List<CartProductGroup>
        get() {
            return visibleProducts.toProductGroups()
        }

    val visibleFittingProducts: List<CartProduct>
        get() {
            return when (payMode) {
                CartPayMode.All -> apiFittingProducts
                CartPayMode.Payment -> apiFittingPaymentProducts
            }
        }

    val visibleFittingProductGroups: List<CartProductGroup>
        get() {
            return visibleFittingProducts.toProductGroups()
        }

    val visibleFittingDeliveryGroups: List<CartFittingDeliveryGroup>
        get() {
            return visibleFittingDeliveries.map { delivery ->
                CartFittingDeliveryGroup(
                    id = delivery.id,
                    fittingType = delivery.fittingType,
                    header = delivery.header,
                    productGroups = delivery.products.toProductGroups()
                )
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

    val totalSummary: String
        get() {
            return summary(products)
        }

    val paymentSummary: String
        get() {
            return summary(paymentProducts)
        }

    val fittingProducts: List<CartProduct>
        get() {
            return products.filter { it.size.isNotBlank() && !it.isSold }
        }

    val fittingPaymentProducts: List<CartProduct>
        get() {
            return fittingProducts.filter { it.isForPayment }
        }

    val fittingProductsCount: Int
        get() {
            return fittingProducts.sumOf { it.itemsCount }
        }

    val fittingPaymentProductsCount: Int
        get() {
            return fittingPaymentProducts.sumOf { it.itemsCount }
        }

    val fittingProductsSummary: String
        get() {
            return summary(fittingProducts)
        }

    val fittingPaymentProductsSummary: String
        get() {
            return summary(fittingPaymentProducts)
        }

    val apiFittingProductsCount: Int
        get() {
            return apiFittingProducts.sumOf { it.itemsCount }
        }

    val apiFittingPaymentProductsCount: Int
        get() {
            return apiFittingPaymentProducts.sumOf { it.itemsCount }
        }

    val apiFittingProductsSummary: String
        get() {
            return summary(apiFittingProducts)
        }

    val apiFittingPaymentProductsSummary: String
        get() {
            return summary(apiFittingPaymentProducts)
        }

    val hasProductsWithoutSize: Boolean
        get() {
            return products.any { it.size.isBlank() && !it.isSold }
        }

    val hasFittingProducts: Boolean
        get() {
            return activeEmployee.hasFittingProducts
        }

    private val paymentProducts: List<CartProduct>
        get() {
            return products.filter { it.isForPayment && !it.isSold }
        }

    private val apiFittingPaymentProducts: List<CartProduct>
        get() {
            return apiFittingProducts.filter { it.isForPayment && !it.isSold }
        }

    private val visibleFittingDeliveries: List<FittingDeliveryData>
        get() {
            return when (payMode) {
                CartPayMode.All -> apiFittingDeliveries
                CartPayMode.Payment -> apiFittingDeliveries.map { delivery ->
                    delivery.copy(
                        products = delivery.products.filter { product -> product.isForPayment && !product.isSold }
                    )
                }.filter { delivery -> delivery.products.isNotEmpty() }
            }
        }

    val cartChatName: String
        get() {
            return activeEmployee.employeeName.ifBlank { DEFAULT_CART_CONSULTANT_NAME }
        }

    val cartChatBrand: String
        get() {
            return activeEmployee.employeeBrand.trim()
        }

    private fun summary(products: List<CartProduct>): String {
        val itemsCount = products.sumOf { it.itemsCount }
        val sum = products.sumOf { (it.priceValue * it.itemsCount).roundToInt() }
        return "$itemsCount ${itemsCount.productsWord} на сумму ${FORMAT_RUB.format(sum.thousandsSeparator)}"
    }

    private fun List<CartProduct>.toProductGroups(): List<CartProductGroup> {
        val lookProducts = filter { !it.lookId.isNullOrEmpty() }
        val noLookProducts = filter { it.lookId.isNullOrEmpty() }
        val lookGroups = lookProducts
            .groupBy { it.lookId.orEmpty() }
            .toSortedMap()
            .map { (_, products) ->
                val firstProduct = products.first()
                CartProductGroup(
                    lookId = firstProduct.lookId,
                    lookName = firstProduct.lookName.orEmpty(),
                    lookImageUrl = firstProduct.lookImageUrl,
                    products = products
                )
            }
        val productGroups = noLookProducts.map { product ->
            CartProductGroup(
                lookId = null,
                lookName = "",
                lookImageUrl = null,
                products = listOf(product)
            )
        }

        return lookGroups + productGroups
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
