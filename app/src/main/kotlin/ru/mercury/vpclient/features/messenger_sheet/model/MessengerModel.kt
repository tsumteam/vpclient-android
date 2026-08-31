package ru.mercury.vpclient.features.messenger_sheet.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.domain.mapper.groupedImageMessages
import ru.mercury.vpclient.shared.mvi.Model

data class MessengerModel(
    val activeEmployeeEntity: EmployeeEntity = EmployeeEntity.Empty,
    val messageEntities: List<MessengerMessageEntity> = emptyList(),
    val messageText: String = "",
    val isAttachSheetVisible: Boolean = false,
    val messagesJob: Job? = null
): Model {

    val groupedMessageEntities: List<MessengerMessageEntity>
        get() = messageEntities.groupedImageMessages()

    val name: String
        get() = activeEmployeeEntity.employeeName

    val brand: String
        get() = activeEmployeeEntity.employeeBrand

    val isSendButtonVisible: Boolean
        get() = messageText.isNotBlank()
}
