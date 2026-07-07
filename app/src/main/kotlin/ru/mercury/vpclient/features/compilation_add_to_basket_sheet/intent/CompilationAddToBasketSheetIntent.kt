package ru.mercury.vpclient.features.compilation_add_to_basket_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationAddToBasketSheetIntent: Intent {
    data object AddToBasketClick: CompilationAddToBasketSheetIntent
    data object DismissRequest: CompilationAddToBasketSheetIntent
    data class AddToBasketProductCheckedChange(val productId: String, val checked: Boolean): CompilationAddToBasketSheetIntent
}
