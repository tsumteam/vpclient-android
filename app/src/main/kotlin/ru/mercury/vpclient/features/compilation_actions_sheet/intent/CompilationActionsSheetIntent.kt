package ru.mercury.vpclient.features.compilation_actions_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationActionsSheetIntent: Intent {
    data object ShowCompilationChatSheet: CompilationActionsSheetIntent
    data object ShowAddToBasketDialog: CompilationActionsSheetIntent
    data object DismissRequest: CompilationActionsSheetIntent
}
