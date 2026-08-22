package ru.mercury.vpclient.features.compilation_add_to_basket_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationAddToBasketIntent: Intent {
    data object DismissClick: CompilationAddToBasketIntent
    data object AddToBasketClick: CompilationAddToBasketIntent
    data class AddToBasketProductCheckedChange(val productId: String, val checked: Boolean): CompilationAddToBasketIntent
}
