package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular11

@Composable
fun CartProductSizeChip(
    size: CartProductSize,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(24.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(start = 8.dp, end = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = size.name,
            style = MaterialTheme.typography.regular11.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp,
                letterSpacing = .2.sp
            )
        )

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Close24,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductSizeChipPreview(
    @PreviewParameter(CartProductSizeChipProvider::class) size: CartProductSize
) {
    CartProductSizeChip(
        size = size,
        onRemoveClick = {}
    )
}

private class CartProductSizeChipProvider: PreviewParameterProvider<CartProductSize> {
    override val values: Sequence<CartProductSize> = sequenceOf(
        CartProductSize(
            id = "1",
            name = "IT 48",
            productId = "1",
            isLastInStock = true
        ),
        CartProductSize(
            id = "2",
            name = "RU 50",
            productId = "1"
        )
    )
}
