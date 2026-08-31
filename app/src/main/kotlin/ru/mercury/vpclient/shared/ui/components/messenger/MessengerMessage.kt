package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayload
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayloadType
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadCompilation
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadImage
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadLook
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadVideo
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun MessengerMessage(
    message: MessengerMessageEntity,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (message.direction) {
        MessengerMessageDirection.System -> {
            val systemProducts = message.payload?.products.orEmpty().filter { product ->
                !product.imageUrl.isNullOrEmpty()
            }

            when {
                message.payload?.type == MessengerMessagePayloadType.Order || systemProducts.isNotEmpty() -> {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        MessengerOrderMessage(
                            state = MessengerOrderMessageState(
                                title = message.payload?.title ?: message.payload?.orderNumber.orEmpty(),
                                text = message.text,
                                products = systemProducts,
                                onProductClick = onProductClick
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MessengerSystemMessage(
                            state = MessengerSystemMessageState(
                                title = message.payload?.title.orEmpty(),
                                text = message.text
                            )
                        )
                    }
                }
            }
        }
        MessengerMessageDirection.Incoming,
        MessengerMessageDirection.Outgoing -> {
            when (message.payload?.type) {
                MessengerMessagePayloadType.Images -> {
                    val imageUrls = message.payload.images.mapNotNull { image ->
                        (image.previewUrl ?: image.imageUrl)?.takeIf { url -> url.isNotEmpty() }
                    }
                    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                    ) {
                        MessengerPhotoMessage(
                            state = MessengerPhotoMessageState(
                                imageUrls = imageUrls,
                                isOutgoing = isOutgoing
                            )
                        )

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                MessengerMessagePayloadType.Videos -> {
                    val previewUrl = message.payload.videos.firstNotNullOfOrNull { video ->
                        (video.previewUrl ?: video.videoUrl)?.takeIf { url -> url.isNotEmpty() }
                    }.orEmpty()
                    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                    ) {
                        MessengerVideoMessage(
                            state = MessengerVideoMessageState(
                                previewUrl = previewUrl,
                                isOutgoing = isOutgoing
                            )
                        )

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                MessengerMessagePayloadType.Product -> {
                    MessengerProductCard(
                        state = MessengerProductCardState(
                            message = message,
                            onProductClick = onProductClick
                        ),
                        modifier = modifier
                    )
                }
                MessengerMessagePayloadType.ClientCompilation -> {
                    val items = message.payload.clientCompilations
                    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                    ) {
                        if (items.size == 1) {
                            MessengerCompilationCard(
                                state = MessengerCompilationCardState(compilation = items.first())
                            )
                        } else {
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items.forEach { compilation ->
                                    MessengerCompilationCard(
                                        state = MessengerCompilationCardState(compilation = compilation)
                                    )
                                }
                            }
                        }

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                MessengerMessagePayloadType.CompilationLook -> {
                    val items = message.payload.compilationLooks
                    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                    ) {
                        if (items.size == 1) {
                            MessengerLookCard(
                                state = MessengerLookCardState(look = items.first())
                            )
                        } else {
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items.forEach { look ->
                                    MessengerLookCard(
                                        state = MessengerLookCardState(look = look)
                                    )
                                }
                            }
                        }

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                MessengerMessagePayloadType.BasketLook -> {
                    val items = message.payload.basketLooks
                    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                    ) {
                        if (items.size == 1) {
                            MessengerLookCard(
                                state = MessengerLookCardState(look = items.first())
                            )
                        } else {
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items.forEach { look ->
                                    MessengerLookCard(
                                        state = MessengerLookCardState(look = look)
                                    )
                                }
                            }
                        }

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                MessengerMessagePayloadType.GiftCard -> {
                    val products = message.payload.products
                    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        products.forEach { product ->
                            MessengerGiftCardMessage(
                                state = MessengerGiftCardMessageState(product = product)
                            )
                        }

                        MessengerMessageMeta(
                            state = MessengerMessageMetaState(
                                createTime = message.createTime,
                                isEdited = message.isEdited,
                                status = message.status
                            ),
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
                else -> {
                    MessengerTextMessage(
                        message = message,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerMessagePreview(
    @PreviewParameter(MessengerMessagePreviewParameterProvider::class) message: MessengerMessageEntity
) {
    MessengerMessage(
        message = message,
        onProductClick = {}
    )
}

private class MessengerMessagePreviewParameterProvider: PreviewParameterProvider<MessengerMessageEntity> {
    override val values: Sequence<MessengerMessageEntity> = sequenceOf(
        MessengerMessageEntity(
            id = 1,
            createTime = "2026-08-26T16:40:00+03:00",
            text = "Уточните, пожалуйста, номер заказа.",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Read,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 2,
            createTime = "2026-08-26T16:38:00+03:00",
            text = "Мария, здравствуйте, прошу прощения, заказ скоро будет сформирован.",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 3,
            createTime = "2026-08-26T16:20:00+03:00",
            text = "Заказ оформлен и передан в обработку",
            direction = MessengerMessageDirection.System,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Order,
                title = "Заказ №0000000"
            )
        ),
        MessengerMessageEntity(
            id = 10,
            createTime = "2026-08-26T16:22:00+03:00",
            text = "Заказ оформлен и передан в обработку",
            direction = MessengerMessageDirection.System,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Order,
                title = "Заказ №1234567",
                orderNumber = "1234567",
                products = List(5) { index ->
                    MessengerPayloadProduct(
                        id = "000$index",
                        brand = "SAINT LAURENT",
                        name = "Кожаная куртка oversize",
                        itemId = "000000$index",
                        price = 189_900.0,
                        colorId = "BLK",
                        colorName = "Чёрный",
                        imageUrl = "https://example.com/order-product-${index + 1}.jpg"
                    )
                }
            )
        ),
        MessengerMessageEntity(
            id = 4,
            createTime = "2026-08-26T16:21:00+03:00",
            text = "Заказ №0000000 оформлен",
            direction = MessengerMessageDirection.System,
            status = null,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 5,
            createTime = "2026-08-26T16:41:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Images,
                images = listOf(
                    MessengerPayloadImage(
                        imageUrl = "https://example.com/photo-1.jpg",
                        previewUrl = "https://example.com/photo-1.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 6,
            createTime = "2026-08-26T16:42:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Images,
                images = List(5) { index ->
                    MessengerPayloadImage(
                        imageUrl = "https://example.com/photo-${index + 1}.jpg",
                        previewUrl = "https://example.com/photo-${index + 1}.jpg"
                    )
                }
            )
        ),
        MessengerMessageEntity(
            id = 7,
            createTime = "2026-08-26T16:44:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Videos,
                videos = listOf(
                    MessengerPayloadVideo(
                        videoUrl = "https://example.com/video.mp4",
                        previewUrl = "https://example.com/video-preview.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 8,
            createTime = "2026-08-26T16:45:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Product,
                products = listOf(
                    MessengerPayloadProduct(
                        id = "0000",
                        brand = "SAINT LAURENT",
                        name = "Кожаная куртка oversize",
                        itemId = "0000000",
                        price = 189_900.0,
                        colorId = "BLK",
                        colorName = "Чёрный",
                        imageUrl = "https://example.com/product-1.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 9,
            createTime = "2026-08-26T16:46:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Product,
                products = List(3) { index ->
                    MessengerPayloadProduct(
                        id = "000$index",
                        brand = "BRUNELLO CUCINELLI",
                        name = "Хлопковая футболка с логотипом",
                        itemId = "000000$index",
                        price = 32_700.0,
                        colorId = "WHT",
                        colorName = "Белый",
                        imageUrl = "https://example.com/product-${index + 1}.jpg"
                    )
                }
            )
        ),
        MessengerMessageEntity(
            id = 11,
            createTime = "2026-08-26T16:47:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.ClientCompilation,
                clientCompilations = listOf(
                    MessengerPayloadCompilation(
                        compilationId = 1,
                        compilationName = "BLV/Hotel",
                        compilationDescription = "Осенняя подборка городских образов",
                        imageUrl = "https://example.com/compilation-1.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 12,
            createTime = "2026-08-26T16:48:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.CompilationLook,
                compilationLooks = listOf(
                    MessengerPayloadLook(
                        id = "look-1",
                        name = "Образ 1",
                        imageUrl = "https://example.com/look-1.jpg",
                        compilationName = "BLV/Hotel"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 13,
            createTime = "2026-08-26T16:49:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.BasketLook,
                basketLooks = listOf(
                    MessengerPayloadLook(
                        id = "look-2",
                        name = "Образ 2",
                        imageUrl = "https://example.com/look-2.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 14,
            createTime = "2026-08-26T16:50:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.GiftCard,
                products = listOf(
                    MessengerPayloadProduct(
                        id = "0000",
                        brand = "VIP PLATINUM",
                        name = "Подарочная карта",
                        itemId = "0000000",
                        price = 10_000.0,
                        colorId = "",
                        colorName = "",
                        imageUrl = "https://example.com/gift-card.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 15,
            createTime = "2026-08-26T16:51:00+03:00",
            text = "Заказ будет готов завтра к 12:00, курьер свяжется с вами заранее.",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                citation = "Здравствуйте! Когда будет готов мой заказ?"
            )
        ),
        MessengerMessageEntity(
            id = 16,
            createTime = "2026-08-26T16:52:00+03:00",
            text = "Да, всё верно, оформляйте доставку на этот адрес.",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Read,
            isEdited = false,
            payload = MessengerMessagePayload(
                citation = "Подтвердите, пожалуйста, адрес доставки: Москва, Тверская улица, дом 1, квартира 10."
            )
        )
    )
}
