package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.FORMAT_PLUS
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular21
import java.util.Locale

data class MessengerProductThumbnailsRowState(
    val products: List<MessengerPayloadProduct>,
    val onProductClick: (String) -> Unit
) {
    val visibleProducts: List<MessengerPayloadProduct>
        get() = if (products.size == 4) products else products.take(3)

    val remainingCount: Int
        get() = (products.size - visibleProducts.size).coerceAtLeast(0)

    val remainingCountText: String
        get() = String.format(Locale.getDefault(), FORMAT_PLUS, remainingCount)

    val isRemainingTileVisible: Boolean
        get() = remainingCount > 0
}

@Composable
fun MessengerProductThumbnailsRow(
    state: MessengerProductThumbnailsRowState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.visibleProducts.forEach { product ->
            Box(
                modifier = Modifier
                    .size(width = 58.dp, height = 72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { state.onProductClick(product.id) }
            ) {
                ClientAsyncImage(
                    imageUrl = product.imageUrl.orEmpty(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }

        if (state.isRemainingTileVisible) {
            Box(
                modifier = Modifier
                    .size(width = 58.dp, height = 72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = .6F)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.remainingCountText,
                    style = MaterialTheme.typography.regular21.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerProductThumbnailsRowPreview(
    @PreviewParameter(MessengerProductThumbnailsRowStatePreviewParameterProvider::class) state: MessengerProductThumbnailsRowState
) {
    MessengerProductThumbnailsRow(
        state = state
    )
}

private class MessengerProductThumbnailsRowStatePreviewParameterProvider: PreviewParameterProvider<MessengerProductThumbnailsRowState> {
    private val products = List(7) { index ->
        MessengerPayloadProduct(
            id = "000$index",
            brand = "SAINT LAURENT",
            name = "Кожаная куртка oversize",
            itemId = "000000$index",
            price = 189_900.0,
            colorId = "BLK",
            colorName = "Чёрный",
            imageUrl = "https://example.com/thumb-${index + 1}.jpg"
        )
    }

    override val values: Sequence<MessengerProductThumbnailsRowState> = sequenceOf(
        MessengerProductThumbnailsRowState(
            products = products.take(2),
            onProductClick = {}
        ),
        MessengerProductThumbnailsRowState(
            products = products.take(4),
            onProductClick = {}
        ),
        MessengerProductThumbnailsRowState(
            products = products,
            onProductClick = {}
        )
    )
}
