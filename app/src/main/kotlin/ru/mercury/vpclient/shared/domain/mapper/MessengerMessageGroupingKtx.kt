package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayloadType
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity

fun List<MessengerMessageEntity>.groupedImageMessages(): List<MessengerMessageEntity> {
    val result = mutableListOf<MessengerMessageEntity>()
    val imageRun = mutableListOf<MessengerMessageEntity>()

    for (message in this) {
        val isGroupableImage = message.direction != MessengerMessageDirection.System &&
            message.payload?.type == MessengerMessagePayloadType.Images
        val extendsRun = isGroupableImage &&
            (imageRun.isEmpty() || imageRun.first().direction == message.direction)

        if (extendsRun) {
            imageRun += message
            continue
        }

        if (imageRun.isNotEmpty()) {
            result += imageRun.singleOrNull() ?: imageRun.last().let { lastMessage ->
                lastMessage.copy(
                    text = "",
                    payload = lastMessage.payload!!.copy(
                        images = imageRun.flatMap { runMessage -> runMessage.payload?.images.orEmpty() }
                    )
                )
            }
            imageRun.clear()
        }

        if (isGroupableImage) {
            imageRun += message
        } else {
            result += message
        }
    }

    if (imageRun.isNotEmpty()) {
        result += imageRun.singleOrNull() ?: imageRun.last().let { lastMessage ->
            lastMessage.copy(
                text = "",
                payload = lastMessage.payload!!.copy(
                    images = imageRun.flatMap { runMessage -> runMessage.payload?.images.orEmpty() }
                )
            )
        }
    }

    return result
}
