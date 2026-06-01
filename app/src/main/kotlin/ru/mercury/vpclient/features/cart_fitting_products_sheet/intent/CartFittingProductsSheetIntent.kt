package ru.mercury.vpclient.features.cart_fitting_products_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingProductsSheetIntent: Intent {
    data class ProductCheckedChange(val productId: String, val checked: Boolean): CartFittingProductsSheetIntent
    data class ConfirmClick(val productIds: List<String>): CartFittingProductsSheetIntent
    data object DismissRequest: CartFittingProductsSheetIntent
}
