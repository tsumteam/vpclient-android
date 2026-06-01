package ru.mercury.vpclient.features.details_message_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsMessageSheetIntent: Intent {
    data class CommentChange(val comment: String): DetailsMessageSheetIntent
    data class SendClick(val comment: String): DetailsMessageSheetIntent
    data object DismissRequest: DetailsMessageSheetIntent
}
