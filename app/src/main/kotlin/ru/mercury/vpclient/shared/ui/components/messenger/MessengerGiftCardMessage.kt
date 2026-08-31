package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.domain.mapper.formatPriceText
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.GiftCard24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15

data class MessengerGiftCardMessageState(
    val product: MessengerPayloadProduct
) {
    val titleText: String
        get() = product.name.ifEmpty { product.brand }

    val isTitleVisible: Boolean
        get() = titleText.isNotEmpty()

    val priceText: String
        get() = product.price.toInt().formatPriceText()

    val isPriceVisible: Boolean
        get() = product.price > 0.0

    val isImageVisible: Boolean
        get() = !product.imageUrl.isNullOrEmpty()
}

@Composable
fun MessengerGiftCardMessage(
    state: MessengerGiftCardMessageState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .widthIn(max = 300.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.isImageVisible) {
            ClientAsyncImage(
                imageUrl = state.product.imageUrl.orEmpty(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 96.dp, height = 60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = GiftCard24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = GiftCard24,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (state.isTitleVisible) {
                    Text(
                        text = state.titleText,
                        style = MaterialTheme.typography.medium15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 15.sp,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }

            if (state.isPriceVisible) {
                Text(
                    text = state.priceText,
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 15.sp,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerGiftCardMessagePreview(
    @PreviewParameter(MessengerGiftCardMessageStatePreviewParameterProvider::class) state: MessengerGiftCardMessageState
) {
    MessengerGiftCardMessage(
        state = state
    )
}

private class MessengerGiftCardMessageStatePreviewParameterProvider: PreviewParameterProvider<MessengerGiftCardMessageState> {
    override val values: Sequence<MessengerGiftCardMessageState> = sequenceOf(
        MessengerGiftCardMessageState(
            product = MessengerPayloadProduct(
                id = "0000",
                brand = "VIP PLATINUM",
                name = "Подарочная карта",
                itemId = "0000000",
                price = 10_000.0,
                colorId = "",
                colorName = "",
                imageUrl = "https://example.com/gift-card.jpg"
            )
        ),
        MessengerGiftCardMessageState(
            product = MessengerPayloadProduct(
                id = "0001",
                brand = "VIP PLATINUM",
                name = "Подарочная карта",
                itemId = "0000001",
                price = 25_000.0,
                colorId = "",
                colorName = "",
                imageUrl = null
            )
        ),
        MessengerGiftCardMessageState(
            product = MessengerPayloadProduct(
                id = "0002",
                brand = "VIP PLATINUM",
                name = "Подарочная карта",
                itemId = "0000002",
                price = 0.0,
                colorId = "",
                colorName = "",
                imageUrl = null
            )
        )
    )
}
