package ru.mercury.vpclient.features.compilation_cart_added_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationCartAddedIntent: Intent {
    data object DismissClick: CompilationCartAddedIntent
    data object CartClick: CompilationCartAddedIntent
}
