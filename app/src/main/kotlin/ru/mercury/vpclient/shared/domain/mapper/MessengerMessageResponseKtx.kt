package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayload
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayloadType
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadCompilation
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadImage
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadLook
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadVideo
import ru.mercury.vpclient.shared.data.network.response.BasketLookPayloadItemResponse
import ru.mercury.vpclient.shared.data.network.response.ClientCompilationPayloadItemResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationLookPayloadItemResponse
import ru.mercury.vpclient.shared.data.network.response.ImagesPayloadItemResponse
import ru.mercury.vpclient.shared.data.network.response.MessageResponse
import ru.mercury.vpclient.shared.data.network.response.PayloadDtoBaseResponse
import ru.mercury.vpclient.shared.data.network.response.ProductPayloadItemResponse
import ru.mercury.vpclient.shared.data.network.response.VideosPayloadItemResponse
import ru.mercury.vpclient.shared.data.network.type.MessageType
import ru.mercury.vpclient.shared.data.network.type.PayloadType
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val List<MessageResponse>.entities: List<MessengerMessageEntity>
    get() = mapNotNull { response -> response.messengerMessageEntity }

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
        val direction = when (type) {
            MessageType.CLIENT -> MessengerMessageDirection.Outgoing
            MessageType.EMPLOYEE -> MessengerMessageDirection.Incoming
            MessageType.SYSTEM -> MessengerMessageDirection.System
            else -> return null
        }
        if (isDeleted == true) return null
        val messageText = payload?.text ?: text.orEmpty()
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
            isEdited = isEdited == true,
            payload = payload?.messengerMessagePayload
        )
    }

private val PayloadDtoBaseResponse.messengerMessagePayload: MessengerMessagePayload?
    get() {
        val hasContent = type != null ||
            !citation.isNullOrEmpty() ||
            !title.isNullOrEmpty() ||
            !orderNumber.isNullOrEmpty() ||
            !products.isNullOrEmpty() || !images.isNullOrEmpty() || !videos.isNullOrEmpty() ||
            !compilationLooks.isNullOrEmpty() || !clientCompilations.isNullOrEmpty() || !basketLooks.isNullOrEmpty()
        if (!hasContent) return null
        return MessengerMessagePayload(
            type = when {
                !orderNumber.isNullOrEmpty() -> MessengerMessagePayloadType.Order
                !images.isNullOrEmpty() -> MessengerMessagePayloadType.Images
                !videos.isNullOrEmpty() -> MessengerMessagePayloadType.Videos
                !clientCompilations.isNullOrEmpty() -> MessengerMessagePayloadType.ClientCompilation
                !compilationLooks.isNullOrEmpty() -> MessengerMessagePayloadType.CompilationLook
                !basketLooks.isNullOrEmpty() -> MessengerMessagePayloadType.BasketLook
                !products.isNullOrEmpty() && type == PayloadType.GIFT_CARD -> MessengerMessagePayloadType.GiftCard
                !products.isNullOrEmpty() -> MessengerMessagePayloadType.Product
                else -> null
            },
            title = title,
            citation = citation,
            citatedText = null,
            orderNumber = orderNumber,
            products = products.orEmpty().map { item -> item.messengerPayloadProduct },
            images = images.orEmpty().map { item -> item.messengerPayloadImage },
            videos = videos.orEmpty().map { item -> item.messengerPayloadVideo },
            compilationLooks = compilationLooks.orEmpty().map { item -> item.messengerPayloadLook },
            clientCompilations = clientCompilations.orEmpty().map { item -> item.messengerPayloadCompilation },
            basketLooks = basketLooks.orEmpty().map { item -> item.messengerPayloadLook }
        )
    }

private val ProductPayloadItemResponse.messengerPayloadProduct: MessengerPayloadProduct
    get() = MessengerPayloadProduct(
        id = id.orEmpty(),
        brand = brand.orEmpty(),
        name = name.orEmpty(),
        itemId = itemId.orEmpty(),
        price = price.orEmpty,
        colorId = colorId.orEmpty(),
        colorName = colorName.orEmpty(),
        imageUrl = imageUrl
    )

private val ImagesPayloadItemResponse.messengerPayloadImage: MessengerPayloadImage
    get() = MessengerPayloadImage(
        imageUrl = imageUrl,
        previewUrl = previewUrl
    )

private val VideosPayloadItemResponse.messengerPayloadVideo: MessengerPayloadVideo
    get() = MessengerPayloadVideo(
        videoUrl = videoUrl,
        previewUrl = previewUrl
    )

private val CompilationLookPayloadItemResponse.messengerPayloadLook: MessengerPayloadLook
    get() = MessengerPayloadLook(
        id = id.orEmpty.toString(),
        name = name.orEmpty(),
        imageUrl = imageUrl,
        compilationName = compilation?.name
    )

private val BasketLookPayloadItemResponse.messengerPayloadLook: MessengerPayloadLook
    get() = MessengerPayloadLook(
        id = id.orEmpty(),
        name = name.orEmpty(),
        imageUrl = imageUrl,
        compilationName = null
    )

private val ClientCompilationPayloadItemResponse.messengerPayloadCompilation: MessengerPayloadCompilation
    get() = MessengerPayloadCompilation(
        compilationId = compilationId.orEmpty,
        compilationName = compilationName.orEmpty(),
        compilationDescription = compilationDescription,
        imageUrl = imageUrl
    )

private val messengerMessageLocale = Locale.forLanguageTag("ru")
private val messengerMessageTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", messengerMessageLocale)
private val messengerMessageDateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", messengerMessageLocale)
