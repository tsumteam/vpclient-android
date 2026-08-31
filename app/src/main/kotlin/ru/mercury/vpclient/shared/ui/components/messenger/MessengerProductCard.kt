package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayload
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayloadType
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.domain.mapper.formatPriceText
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular14

data class MessengerProductCardState(
    val message: MessengerMessageEntity,
    val onProductClick: (String) -> Unit
) {
    val isOutgoing: Boolean
        get() = message.direction == MessengerMessageDirection.Outgoing

    val isTextVisible: Boolean
        get() = message.text.isNotEmpty()
}

@Composable
fun MessengerProductCard(
    state: MessengerProductCardState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = if (state.isOutgoing) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 328.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = if (state.isOutgoing) 16.dp else 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (state.isOutgoing) 0.dp else 16.dp,
                        bottomStart = 16.dp
                    )
                )
                .background(
                    when {
                        state.isOutgoing -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isTextVisible) {
                Text(
                    text = state.message.text,
                    style = MaterialTheme.typography.regular14.copy(
                        color = when {
                            state.isOutgoing -> MaterialTheme.colorScheme.background
                            else -> MaterialTheme.colorScheme.onBackground
                        },
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )
            }

            state.message.payload?.products.orEmpty().forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { state.onProductClick(product.id) },
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClientAsyncImage(
                        imageUrl = product.imageUrl.orEmpty(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 58.dp, height = 72.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Column(
                        modifier = Modifier.weight(1F)
                    ) {
                        if (product.brand.isNotEmpty()) {
                            Text(
                                text = product.brand,
                                style = MaterialTheme.typography.regular12.copy(
                                    color = when {
                                        state.isOutgoing -> MaterialTheme.colorScheme.background
                                        else -> MaterialTheme.colorScheme.onBackground
                                    },
                                    lineHeight = 16.sp,
                                    letterSpacing = .2.sp
                                )
                            )
                        }

                        if (product.name.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.regular11.copy(
                                    color = when {
                                        state.isOutgoing -> MaterialTheme.colorScheme.background
                                        else -> MaterialTheme.colorScheme.onBackground
                                    },
                                    lineHeight = 11.sp
                                )
                            )
                        }

                        if (product.price > 0.0) {
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = product.price.toInt().formatPriceText(),
                                style = MaterialTheme.typography.regular12.copy(
                                    color = when {
                                        state.isOutgoing -> MaterialTheme.colorScheme.background
                                        else -> MaterialTheme.colorScheme.onBackground
                                    },
                                    lineHeight = 16.sp,
                                    letterSpacing = .2.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        MessengerMessageMeta(
            state = MessengerMessageMetaState(
                createTime = state.message.createTime,
                isEdited = state.message.isEdited,
                status = state.message.status
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerProductCardPreview(
    @PreviewParameter(MessengerProductCardPreviewParameterProvider::class) state: MessengerProductCardState
) {
    MessengerProductCard(
        state = state
    )
}

private class MessengerProductCardPreviewParameterProvider: PreviewParameterProvider<MessengerProductCardState> {
    override val values: Sequence<MessengerProductCardState> = sequenceOf(
        MessengerProductCardState(
            message = MessengerMessageEntity(
                id = 1,
                createTime = "2026-08-26T16:45:00+03:00",
                text = "Добрый день!",
                direction = MessengerMessageDirection.Outgoing,
                status = MessengerMessageStatus.Read,
                isEdited = false,
                payload = MessengerMessagePayload(
                    type = MessengerMessagePayloadType.Product,
                    products = listOf(
                        MessengerPayloadProduct(
                            id = "0000",
                            brand = "DOLCE & GABBANA",
                            name = "Серьги",
                            itemId = "0000000",
                            price = 1_600_000.0,
                            colorId = "",
                            colorName = "",
                            imageUrl = "https://example.com/product-1.jpg"
                        ),
                        MessengerPayloadProduct(
                            id = "0001",
                            brand = "MVST",
                            name = "Платье",
                            itemId = "0000001",
                            price = 290_000.0,
                            colorId = "",
                            colorName = "",
                            imageUrl = "https://example.com/product-2.jpg"
                        )
                    )
                )
            ),
            onProductClick = {}
        ),
        MessengerProductCardState(
            message = MessengerMessageEntity(
                id = 2,
                createTime = "2026-08-26T16:46:00+03:00",
                text = "",
                direction = MessengerMessageDirection.Incoming,
                status = null,
                isEdited = false,
                payload = MessengerMessagePayload(
                    type = MessengerMessagePayloadType.Product,
                    products = listOf(
                        MessengerPayloadProduct(
                            id = "0002",
                            brand = "SAINT LAURENT",
                            name = "Кожаная куртка oversize",
                            itemId = "0000002",
                            price = 189_900.0,
                            colorId = "",
                            colorName = "",
                            imageUrl = "https://example.com/product-3.jpg"
                        )
                    )
                )
            ),
            onProductClick = {}
        ),
        MessengerProductCardState(
            message = MessengerMessageEntity(
                id = 3,
                createTime = "2026-08-26T16:47:00+03:00",
                text = "",
                direction = MessengerMessageDirection.Outgoing,
                status = MessengerMessageStatus.Sent,
                isEdited = false,
                payload = MessengerMessagePayload(
                    type = MessengerMessagePayloadType.Product,
                    products = List(3) { index ->
                        MessengerPayloadProduct(
                            id = "000$index",
                            brand = "BRUNELLO CUCINELLI",
                            name = "Хлопковая футболка",
                            itemId = "000000$index",
                            price = 32_700.0,
                            colorId = "",
                            colorName = "",
                            imageUrl = "https://example.com/product-${index + 1}.jpg"
                        )
                    }
                )
            ),
            onProductClick = {}
        )
    )
}
