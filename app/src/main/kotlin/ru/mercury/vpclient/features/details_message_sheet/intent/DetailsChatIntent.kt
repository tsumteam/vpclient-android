package ru.mercury.vpclient.features.details_message_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsChatIntent: Intent {
    data class CommentChange(val comment: String): DetailsChatIntent
    data class SendClick(val comment: String): DetailsChatIntent
    data object DismissRequest: DetailsChatIntent
}
