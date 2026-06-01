package ru.mercury.vpclient.features.cart_edit_product_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartEditProductSheetIntent: Intent {
    data class ActionClick(val index: Int): CartEditProductSheetIntent
    data object DismissRequest: CartEditProductSheetIntent
}
