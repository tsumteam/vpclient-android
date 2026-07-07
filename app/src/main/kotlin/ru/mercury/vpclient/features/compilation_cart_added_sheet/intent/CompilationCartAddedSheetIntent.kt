package ru.mercury.vpclient.features.compilation_cart_added_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationCartAddedSheetIntent: Intent {
    data object ReturnToCompilationClick: CompilationCartAddedSheetIntent
    data object CartClick: CompilationCartAddedSheetIntent
    data object DismissRequest: CompilationCartAddedSheetIntent
}
