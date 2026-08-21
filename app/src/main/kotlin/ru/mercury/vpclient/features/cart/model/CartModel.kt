package ru.mercury.vpclient.features.cart.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart_edit_product_sheet.model.CartEditProductModel
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.model.CartFittingEditProductModel
import ru.mercury.vpclient.features.cart_fitting_sheet.model.CartFittingModel
import ru.mercury.vpclient.features.color_picker_sheet.model.ColorPickerModel
import ru.mercury.vpclient.features.quantity_picker_sheet.model.QuantityPickerModel
import ru.mercury.vpclient.shared.data.CART_DRAG_AND_DROP_ENABLED
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.data.entity.CartFittingDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductGroup
import ru.mercury.vpclient.shared.data.entity.CartQuantityItem
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import ru.mercury.vpclient.shared.domain.mapper.thousandsSeparator
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import kotlin.math.roundToInt

data class CartModel(
    val initialPage: Int = CART_PAGE_INDEX,
    val payMode: CartPayMode = CartPayMode.All,
    val isCartInitialLoading: Boolean = false,
    val isFittingInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val products: List<CartProduct> = emptyList(),
    val fittingCount: Int = 0,
    val apiFittingProducts: List<CartProduct> = emptyList(),
    val apiFittingDeliveries: List<FittingDeliveryData> = emptyList(),
    val editProduct: CartProduct? = null,
    val fittingEditProduct: CartProduct? = null,
    val isCartFittingSheetVisible: Boolean = false,
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
    val isFittingSheetClientFeminine: Boolean = false,
    val isCartEmptyOrderDialogVisible: Boolean = false,
    val isCartFittingEmptyOrderDialogVisible: Boolean = false
): Model {
    val isEditProductSheetVisible: Boolean
        get() = editProduct != null

    val editProductActions: List<Pair<Int, CartIntent>>
        get() {
            val product = editProduct ?: return emptyList()
            return buildList {
                if (!product.isSold && product.isSizeSelectionAvailable && product.sizeItems.size < 2) {
                    add(ClientStrings.CartEditAddSize to CartIntent.AddSizeClick(product))
                }
                if (product.isSizeSelectionAvailable) {
                    add(ClientStrings.CartEditSelectSize to CartIntent.ShowSizePicker(product))
                }
                if (!product.isSold) {
                    add(ClientStrings.CartEditChangeQuantity to CartIntent.ShowQuantityPicker(product))
                }
                add(ClientStrings.CartEditChangeColor to CartIntent.ShowColorPicker(product))
            }
        }

    val editProductModel: CartEditProductModel
        get() = CartEditProductModel(
            actions = editProductActions.map { it.first }
        )

    val isCartFittingEditProductSheetVisible: Boolean
        get() = fittingEditProduct != null

    val fittingEditProductModel: CartFittingEditProductModel
        get() = CartFittingEditProductModel(
            isSizeSelectionAvailable = fittingEditProduct?.isSizeSelectionAvailable.orEmpty
        )

    val isSizePickerSheetVisible: Boolean
        get() = sizePickerProduct != null

    val isColorPickerSheetVisible: Boolean
        get() = colorPickerProduct != null

    val isQuantityPickerSheetVisible: Boolean
        get() = quantityPickerProduct != null

    val visibleSizePickerItems: List<ProductAvailableSizeEntity>
        get() {
            val sizes = sizePickerSizes ?: return emptyList()
            val product = sizePickerProduct ?: return emptyList()
            return sizes.items.filterNot { size ->
                product.sizeItems.any { item -> item.id == size.sizeId }
            }
        }

    val sizePickerState: SizeSelectorState
        get() {
            val sizes = sizePickerSizes ?: return SizeSelectorState.Empty
            if (sizePickerProduct == null) return SizeSelectorState.Empty
            return SizeSelectorState(
                sizes = visibleSizePickerItems.map { size ->
                    val displayText = size.sizeFullName.orEmpty().ifBlank { size.sizeId }
                    val displayParts = displayText.split("|")
                    SizeState(
                        topText = displayParts.firstOrNull()?.shortSizeText() ?: size.sizeId.shortSizeText(),
                        bottomText = size.russianSize?.shortSizeText()
                            ?: displayParts.getOrNull(1)?.shortSizeText()
                            ?: "-",
                        selected = size.sizeId == sizePickerSelectedId,
                        enabled = size.inStock
                    )
                },
                topText = sizes.countryCode.orEmpty(),
                bottomText = "RU",
                isSizeTableVisible = !sizes.sizeTableTitle.isNullOrEmpty() && !sizes.sizeTableUrl.isNullOrEmpty()
            )
        }

    val colorPickerColorsState: List<ProductAvailableColor>
        get() {
            val colors = colorPickerColors ?: return emptyList()
            return colors.map { color ->
                color.copy(selected = color.id == colorPickerSelectedId)
            }
        }

    val colorPickerModel: ColorPickerModel
        get() = ColorPickerModel(colors = colorPickerColorsState)

    val quantityPickerValues: List<CartQuantityItem>
        get() {
            val product = quantityPickerProduct ?: return emptyList()
            val maxAvailableQuantity = product.sizeItems
                .map { size -> size.availableStockQuantity }
                .filter { quantity -> quantity > 0 }
                .minOrNull()
            val maxValue = maxOf(maxAvailableQuantity ?: product.quantity, product.quantity)
            val selectedValue = (quantityPickerSelectedValue ?: product.quantity).coerceIn(1, maxValue)
            return (1..maxValue).map { value ->
                CartQuantityItem(
                    value = value,
                    selected = value == selectedValue
                )
            }
        }

    val quantityPickerModel: QuantityPickerModel
        get() = QuantityPickerModel(quantities = quantityPickerValues)

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

    val cartFittingModel: CartFittingModel
        get() = CartFittingModel(
            clientName = fittingSheetClientName,
            clientFeminine = isFittingSheetClientFeminine,
            allProductsCount = fittingProductsCount,
            allProductsSummary = fittingProductsSummary,
            paymentProductsCount = fittingPaymentProductsCount,
            paymentProductsSummary = fittingPaymentProductsSummary,
            hasProductsWithoutSize = hasProductsWithoutSize
        )

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
        get() = fittingCount > 0

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
        return sizeText.split(PREFIX_SPACE).lastOrNull { it.isNotBlank() } ?: sizeText
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
