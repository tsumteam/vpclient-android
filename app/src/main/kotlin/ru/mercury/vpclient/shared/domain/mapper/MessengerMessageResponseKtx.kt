package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.network.response.MessageResponse
import ru.mercury.vpclient.shared.data.network.type.MessageType
import ru.mercury.vpclient.shared.data.network.type.PayloadType
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val List<MessageResponse>.entities: List<MessengerMessageEntity>
    get() {
        return mapNotNull { response -> response.messengerMessageEntity }
    }

fun String.messengerMessageDateText(
    now: ZonedDateTime = ZonedDateTime.now()
): String {
    val dateTime = runCatching { OffsetDateTime.parse(this).atZoneSameInstant(now.zone) }.getOrNull()
        ?: runCatching { LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(now.zone) }.getOrNull()
        ?: return this
    val timeText = dateTime.format(messengerMessageTimeFormatter)
    return when (dateTime.toLocalDate()) {
        now.toLocalDate() -> timeText
        now.toLocalDate().minusDays(1) -> "Вчера, $timeText"
        else -> dateTime.format(messengerMessageDateTimeFormatter)
    }
}

private val MessageResponse.messengerMessageEntity: MessengerMessageEntity?
    get() {
        val messageText = payload?.text ?: text
        val direction = when (type) {
            MessageType.CLIENT -> MessengerMessageDirection.Outgoing
            MessageType.EMPLOYEE -> MessengerMessageDirection.Incoming
            else -> return null
        }
        val isMediaPayload = payload?.type == PayloadType.IMAGES || payload?.type == PayloadType.VIDEOS
        if (isDeleted == true || isMediaPayload || messageText.isNullOrBlank()) return null
        return MessengerMessageEntity(
            id = messageId ?: localMessageId.hashCode().toLong(),
            createTime = createTime ?: localCreateTime.orEmpty(),
            text = messageText,
            direction = direction,
            status = when {
                direction != MessengerMessageDirection.Outgoing || showChecks == false -> null
                isRead == true -> MessengerMessageStatus.Read
                isReceived == true -> MessengerMessageStatus.Received
                else -> MessengerMessageStatus.Sent
            },
            isEdited = isEdited == true
        )
    }

private val messengerMessageLocale = Locale.forLanguageTag("ru")
private val messengerMessageTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", messengerMessageLocale)
private val messengerMessageDateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", messengerMessageLocale)
