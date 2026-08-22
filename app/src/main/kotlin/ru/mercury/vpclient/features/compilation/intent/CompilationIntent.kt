package ru.mercury.vpclient.features.compilation.intent

import ru.mercury.vpclient.features.compilation_actions_sheet.intent.CompilationActionsIntent
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.intent.CompilationAddToBasketIntent
import ru.mercury.vpclient.features.compilation_benefit_sheet.intent.CompilationBenefitIntent
import ru.mercury.vpclient.features.compilation_cart_added_sheet.intent.CompilationCartAddedIntent
import ru.mercury.vpclient.features.compilation_chat_sheet.intent.CompilationChatIntent
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsMessageIntent
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
    data object ShowAddToBasketDialog: CompilationIntent
    data object ShowBenefitSheet: CompilationIntent
    data class PageChange(val index: Int): CompilationIntent
    data class ImageClick(val initialPage: Int): CompilationIntent
    data class ProductClick(val id: String): CompilationIntent
    data class ProductBasketClick(val productEntity: CatalogFilterProductsEntity): CompilationIntent
    data class ProductMessageClick(val productEntity: CatalogFilterProductsEntity): CompilationIntent
    data class OnCompilationAddToBasketIntent(val intent: CompilationAddToBasketIntent): CompilationIntent
    data class OnCompilationActionsIntent(val intent: CompilationActionsIntent): CompilationIntent
    data class OnCompilationChatIntent(val intent: CompilationChatIntent): CompilationIntent
    data class OnDetailsMessageIntent(val intent: DetailsMessageIntent): CompilationIntent
    data class OnCompilationCartAddedIntent(val intent: CompilationCartAddedIntent): CompilationIntent
    data class OnCompilationBenefitIntent(val intent: CompilationBenefitIntent): CompilationIntent
}
