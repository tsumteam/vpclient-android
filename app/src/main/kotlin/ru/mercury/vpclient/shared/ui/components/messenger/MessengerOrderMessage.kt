package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

data class MessengerOrderMessageState(
    val title: String,
    val text: String,
    val products: List<MessengerPayloadProduct>,
    val onProductClick: (String) -> Unit
) {
    val isTitleVisible: Boolean
        get() = title.isNotEmpty()

    val isTextVisible: Boolean
        get() = text.isNotEmpty()

    val areThumbnailsVisible: Boolean
        get() = products.isNotEmpty()
}

@Composable
fun MessengerOrderMessage(
    state: MessengerOrderMessageState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (state.isTitleVisible) {
            Text(
                text = state.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        if (state.isTextVisible) {
            Text(
                text = state.text,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        if (state.areThumbnailsVisible) {
            MessengerProductThumbnailsRow(
                state = MessengerProductThumbnailsRowState(
                    products = state.products,
                    onProductClick = state.onProductClick
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerOrderMessagePreview(
    @PreviewParameter(MessengerOrderMessageStatePreviewParameterProvider::class) state: MessengerOrderMessageState
) {
    MessengerOrderMessage(
        state = state,
        modifier = Modifier.fillMaxWidth()
    )
}

private class MessengerOrderMessageStatePreviewParameterProvider: PreviewParameterProvider<MessengerOrderMessageState> {
    override val values: Sequence<MessengerOrderMessageState> = sequenceOf(
        MessengerOrderMessageState(
            title = "Заказ №1234567",
            text = "Заказ оформлен и передан в обработку",
            products = List(5) { index ->
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
            },
            onProductClick = {}
        ),
        MessengerOrderMessageState(
            title = "Заказ №1234567",
            text = "Заказ №1234567 оформлен",
            products = emptyList(),
            onProductClick = {}
        )
    )
}
