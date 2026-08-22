package ru.mercury.vpclient.features.fitting_products_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingProductsIntent: Intent {
    data object DismissClick: FittingProductsIntent
    data object CollectProducts: FittingProductsIntent
    data object ConfirmClick: FittingProductsIntent
    data class ProductCheckedChange(val productId: String, val checked: Boolean): FittingProductsIntent
}
