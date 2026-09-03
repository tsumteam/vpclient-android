package ru.mercury.vpclient.features.messenger_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MessengerIntent: Intent {
    data object DismissClick: MessengerIntent
    data object CollectActiveEmployee: MessengerIntent
    data object PollNewMessages: MessengerIntent
    data object CallClick: MessengerIntent
    data object AttachClick: MessengerIntent
    data object DismissAttachSheet: MessengerIntent
    data object AttachGalleryClick: MessengerIntent
    data object AttachCartProductsClick: MessengerIntent
    data object AttachCatalogClick: MessengerIntent
    data object MicClick: MessengerIntent
    data object SendClick: MessengerIntent
    data class MessageTextChange(val text: String): MessengerIntent
    data class ProductClick(val productId: String): MessengerIntent
    data class ReplyMessageClick(val messageId: Long): MessengerIntent
    data class CopyMessageClick(val text: String): MessengerIntent
    data class EditMessageClick(val messageId: Long): MessengerIntent
    data class DeleteMessageClick(val messageId: Long): MessengerIntent
    data class ResendMessageClick(val messageId: Long): MessengerIntent
}
