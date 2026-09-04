package ru.mercury.vpclient.features.messenger_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.mvi.Model

data class MessengerModel(
    val activeEmployeeEntity: EmployeeEntity = EmployeeEntity.Empty,
    val messageText: String = "",
    val isAttachSheetVisible: Boolean = false,
    val editingMessageId: Long? = null,
    val editingOriginalText: String = "",
    val replyMessageId: Long? = null,
    val replyAuthorName: String = "",
    val replyText: String = ""
): Model {

    val name: String
        get() = activeEmployeeEntity.employeeName

    val brand: String
        get() = activeEmployeeEntity.employeeBrand

    val isSendButtonVisible: Boolean
        get() = messageText.isNotBlank()

    val isEditing: Boolean
        get() = editingMessageId != null

    val isReplying: Boolean
        get() = replyMessageId != null
}
