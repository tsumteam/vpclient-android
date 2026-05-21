package ru.mercury.vpclient.features.cart.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.SizeSelectorState
import ru.mercury.vpclient.shared.data.entity.SizeState
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.thousandsSeparator
import ru.mercury.vpclient.shared.mvi.Model
import kotlin.math.roundToInt

private const val DEFAULT_CART_CONSULTANT_NAME = "Персональный менеджер"

data class CartModel(
    val payMode: CartPayMode = CartPayMode.All,
    val viewMode: CartViewMode = CartViewMode.List,
    val isViewModeSwitcherVisible: Boolean = false,
    val isRefreshing: Boolean = false,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val products: List<CartProduct> = emptyList(),
    val selectSizeProduct: CartProduct? = null,
    val sizePickerProduct: CartProduct? = null,
    val sizePickerSizes: ProductAvailableSizesEntity? = null,
    val sizePickerSelectedId: String? = null,
    val selectedAlternativeId: String? = null,
    val paySwitchJob: Job? = null,
    val sizePickerJob: Job? = null
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
            val products = visibleProducts
            val lookProducts = products.filter { !it.lookId.isNullOrEmpty() }
            val noLookProducts = products.filter { it.lookId.isNullOrEmpty() }
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

    private val paymentProducts: List<CartProduct>
        get() {
            return products.filter { it.isForPayment && !it.isSold }
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
