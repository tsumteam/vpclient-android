package ru.mercury.vpclient.features.compilation.intent

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationIntent: Intent {
    data object CollectCompilation: CompilationIntent
    data object CollectCompilationProducts: CompilationIntent
    data object CollectCartProducts: CompilationIntent
    data object CollectCartCount: CompilationIntent
    data object LoadCompilation: CompilationIntent
    data object BackClick: CompilationIntent
    data object CartClick: CompilationIntent
    data object MenuClick: CompilationIntent
    data object HideMenuDialog: CompilationIntent
    data object ShowCompilationChatSheet: CompilationIntent
    data object HideCompilationChatSheet: CompilationIntent
    data object ShowAddToBasketDialog: CompilationIntent
    data object HideAddToBasketDialog: CompilationIntent
    data object HideCartAddedSheet: CompilationIntent
    data object CartAddedSheetCartClick: CompilationIntent
    data object ShowBenefitSheet: CompilationIntent
    data object HideBenefitSheet: CompilationIntent
    data object AddToBasketClick: CompilationIntent
    data object HideMessageSheet: CompilationIntent
    data class PageChange(val index: Int): CompilationIntent
    data class ImageClick(val initialPage: Int): CompilationIntent
    data class ProductClick(val id: String): CompilationIntent
    data class ProductBasketClick(val productEntity: CatalogFilterProductsEntity): CompilationIntent
    data class ProductMessageClick(val productEntity: CatalogFilterProductsEntity): CompilationIntent
    data class AddToBasketProductCheckedChange(val productId: String, val checked: Boolean): CompilationIntent
    data class CompilationChatSendClick(val comment: String): CompilationIntent
}
