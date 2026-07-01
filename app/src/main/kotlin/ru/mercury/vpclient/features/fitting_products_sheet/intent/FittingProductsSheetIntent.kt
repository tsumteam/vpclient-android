package ru.mercury.vpclient.features.fitting_products_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingProductsSheetIntent: Intent {
    data object CollectProducts: FittingProductsSheetIntent
    data object ConfirmClick: FittingProductsSheetIntent
    data object DismissRequest: FittingProductsSheetIntent
    data class ProductCheckedChange(val productId: String, val checked: Boolean): FittingProductsSheetIntent
}
