package ru.mercury.vpclient.features.compilation_actions_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationActionsIntent: Intent {
    data object ShowCompilationChatSheet: CompilationActionsIntent
    data object ShowAddToBasketDialog: CompilationActionsIntent
    data object DismissClick: CompilationActionsIntent
}
