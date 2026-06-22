package ru.mercury.vpclient.features.cart.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.cart_quantity_sheet.model.CartQuantityItem
import ru.mercury.vpclient.shared.data.CART_DRAG_AND_DROP_ENABLED
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.entity.CartFittingDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductGroup
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.mapper.thousandsSeparator
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import kotlin.math.roundToInt

data class CartModel(
    val initialPage: Int = CART_PAGE_INDEX,
    val payMode: CartPayMode = CartPayMode.All,
    val isRefreshing: Boolean = false,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val products: List<CartProduct> = emptyList(),
    val apiFittingProducts: List<CartProduct> = emptyList(),
    val apiFittingDeliveries: List<FittingDeliveryData> = emptyList(),
    val editProduct: CartProduct? = null,
    val fittingEditProduct: CartProduct? = null,
    val isFittingSheetVisible: Boolean = false,
    val isFittingProductsSheetVisible: Boolean = false,
    val sizePickerProduct: CartProduct? = null,
    val sizePickerSizes: ProductAvailableSizesEntity? = null,
    val sizePickerSelectedId: String? = null,
    val sizePickerForFitting: Boolean = false,
    val sizePickerAddSize: Boolean = false,
    val colorPickerProduct: CartProduct? = null,
    val colorPickerColors: List<ProductAvailableColor>? = null,
    val colorPickerSelectedId: String? = null,
    val colorPickerForFitting: Boolean = false,
    val quantityPickerProduct: CartProduct? = null,
    val quantityPickerSelectedValue: Int? = null,
    val selectedAlternativeId: String? = null,
    val paySwitchJob: Job? = null,
    val sizePickerJob: Job? = null,
    val fittingSheetClientName: String = "",
    val isFittingSheetClientFeminine: Boolean = false
): Model {
    val sizePickerState: SizeSelectorState
        get() {
            val sizes = sizePickerSizes ?: return SizeSelectorState.Empty
            val product = sizePickerProduct ?: return SizeSelectorState.Empty
            return SizeSelectorState(
                sizes = sizes.items.map { size ->
                    val displayText = size.sizeFullName.orEmpty().ifBlank { size.sizeId }
                    val displayParts = displayText.split("|")
                    SizeState(
                        topText = displayParts.firstOrNull()?.shortSizeText() ?: size.sizeId.shortSizeText(),
                        bottomText = size.russianSize?.shortSizeText()
                            ?: displayParts.getOrNull(1)?.shortSizeText()
                            ?: "-",
                        selected = size.sizeId == sizePickerSelectedId,
                        enabled = size.inStock && !product.sizeItems.any { item -> item.id == size.sizeId }
                    )
                },
                topText = sizes.countryCode.orEmpty(),
                bottomText = "RU",
                isSizeTableVisible = false
            )
        }

    val colorPickerColorsState: List<ProductAvailableColor>
        get() {
            val colors = colorPickerColors ?: return emptyList()
            return colors.map { color ->
                color.copy(selected = color.id == colorPickerSelectedId)
            }
        }

    val quantityPickerValues: List<CartQuantityItem>
        get() {
            val product = quantityPickerProduct ?: return emptyList()
            val selectedValue = quantityPickerSelectedValue ?: product.quantity
            return (1..maxOf(10, product.quantity)).map { value ->
                CartQuantityItem(
                    value = value,
                    selected = value == selectedValue
                )
            }
        }

    val visibleProducts: List<CartProduct>
        get() = when (payMode) {
            CartPayMode.All -> products
            CartPayMode.Payment -> paymentProducts
        }

    val visibleProductGroups: List<CartProductGroup>
        get() = visibleProducts.toProductGroups()

    val visibleFittingProducts: List<CartProduct>
        get() = when (payMode) {
            CartPayMode.All -> apiFittingProducts
            CartPayMode.Payment -> apiFittingPaymentProducts
        }

    val visibleFittingProductGroups: List<CartProductGroup>
        get() = visibleFittingProducts.toProductGroups()

    val visibleFittingDeliveryGroups: List<CartFittingDeliveryGroup>
        get() = visibleFittingDeliveries.map { delivery ->
            CartFittingDeliveryGroup(
                id = delivery.id,
                fittingType = delivery.fittingType,
                header = delivery.header,
                productGroups = delivery.products.toProductGroups()
            )
        }

    val allItemsCount: Int
        get() = products.sumOf { it.itemsCount }

    val paymentItemsCount: Int
        get() = paymentProducts.sumOf { it.itemsCount }

    val totalSummary: String
        get() = summary(products)

    val paymentSummary: String
        get() = summary(paymentProducts)

    val fittingProducts: List<CartProduct>
        get() = products.filter { it.size.isNotBlank() && !it.isSold }

    val fittingPaymentProducts: List<CartProduct>
        get() = fittingProducts.filter { it.isForPayment }

    val fittingProductsCount: Int
        get() = fittingProducts.sumOf { it.itemsCount }

    val fittingPaymentProductsCount: Int
        get() = fittingPaymentProducts.sumOf { it.itemsCount }

    val fittingProductsSummary: String
        get() = summary(fittingProducts)

    val fittingPaymentProductsSummary: String
        get() = summary(fittingPaymentProducts)

    val apiFittingProductsCount: Int
        get() = apiFittingProducts.sumOf { it.itemsCount }

    val apiFittingPaymentProductsCount: Int
        get() = apiFittingPaymentProducts.sumOf { it.itemsCount }

    val apiFittingProductsSummary: String
        get() = summary(apiFittingProducts)

    val apiFittingPaymentProductsSummary: String
        get() = summary(apiFittingPaymentProducts)

    val hasProductsWithoutSize: Boolean
        get() = products.any { it.size.isBlank() && !it.isSold }

    val hasFittingProducts: Boolean
        get() = activeEmployee.hasFittingProducts

    val dragEnabled: Boolean
        get() = CART_DRAG_AND_DROP_ENABLED && payMode == CartPayMode.All && !isRefreshing

    private val paymentProducts: List<CartProduct>
        get() = products.filter { it.isForPayment && !it.isSold }

    private val apiFittingPaymentProducts: List<CartProduct>
        get() = apiFittingProducts.filter { it.isForPayment && !it.isSold }

    private val visibleFittingDeliveries: List<FittingDeliveryData>
        get() = when (payMode) {
            CartPayMode.All -> apiFittingDeliveries
            CartPayMode.Payment -> apiFittingDeliveries.map { delivery ->
                delivery.copy(
                    products = delivery.products.filter { product -> product.isForPayment && !product.isSold }
                )
            }.filter { delivery -> delivery.products.isNotEmpty() }
        }

    val cartChatBrand: String
        get() = activeEmployee.employeeBrand.trim()

    private fun summary(products: List<CartProduct>): String {
        val itemsCount = products.sumOf { it.itemsCount }
        val sum = products.sumOf { (it.priceValue * it.itemsCount).roundToInt() }
        return "$itemsCount ${itemsCount.productsWord} на сумму ${FORMAT_RUB.format(sum.thousandsSeparator)}"
    }

    private fun String.shortSizeText(): String {
        val sizeText = substringBefore("|")
            .substringAfterLast("—")
            .substringAfterLast("-")
            .trim()
        return sizeText.split(" ").lastOrNull { it.isNotBlank() } ?: sizeText
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
        get() = quantity * sizeCount

    private val Int.productsWord: String
        get() = when {
            this % 100 in 11..14 -> "товаров"
            this % 10 == 1 -> "товар"
            this % 10 in 2..4 -> "товара"
            else -> "товаров"
        }

    companion object {
        const val CART_PAGE_ANIMATION_DURATION = 350
        const val CART_PAGE_COUNT = 2
        const val CART_PAGE_INDEX = 0
        const val FITTING_PAGE_INDEX = 1
    }
}
