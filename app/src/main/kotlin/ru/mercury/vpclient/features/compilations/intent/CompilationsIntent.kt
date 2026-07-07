package ru.mercury.vpclient.features.compilations.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationsIntent: Intent {
    data object CollectCartCount: CompilationsIntent
    data object CollectFittingCount: CompilationsIntent
    data object CollectCartProducts: CompilationsIntent
    data object CollectActiveEmployee: CompilationsIntent
    data object CollectCompilationEntities: CompilationsIntent
    data object LoadCompilationsClient: CompilationsIntent
    data object SearchClick: CompilationsIntent
    data object CartClick: CompilationsIntent
    data object FittingClick: CompilationsIntent
    data object MessengerClick: CompilationsIntent
    data object HideCompilationChatSheet: CompilationsIntent
    data class CompilationClick(val id: Int): CompilationsIntent
    data class CompilationChatClick(val id: Int): CompilationsIntent
    data class CompilationChatSendClick(val comment: String): CompilationsIntent
}
