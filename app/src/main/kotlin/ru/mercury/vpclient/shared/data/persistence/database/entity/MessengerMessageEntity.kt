package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayload
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus

@Entity(
    tableName = "MessengerMessage",
    primaryKeys = ["id"]
)
data class MessengerMessageEntity(
    val id: Long,
    val createTime: String,
    val text: String,
    val direction: MessengerMessageDirection,
    val status: MessengerMessageStatus?,
    val isEdited: Boolean,
    val payload: MessengerMessagePayload? = null
)
