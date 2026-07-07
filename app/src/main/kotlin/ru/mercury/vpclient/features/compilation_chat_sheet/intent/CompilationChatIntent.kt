package ru.mercury.vpclient.features.compilation_chat_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationChatIntent: Intent {
    data object DismissRequest: CompilationChatIntent
    data class CommentChange(val comment: String): CompilationChatIntent
    data class SendClick(val comment: String): CompilationChatIntent
}
