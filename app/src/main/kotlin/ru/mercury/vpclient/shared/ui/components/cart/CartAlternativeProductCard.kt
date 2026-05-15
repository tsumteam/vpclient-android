package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Cancel14
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.secondary5

@Composable
fun CartAlternativeProductCard(
    product: CartProduct,
    modifier: Modifier = Modifier,
    isStartBorderVisible: Boolean = true,
    onClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {}
) {
    val borderColor = MaterialTheme.colorScheme.secondary5
    val borderWidth = 1.dp

    Box(
        modifier = modifier
            .size(width = 112.dp, height = 156.dp)
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                val strokeWidth = borderWidth.toPx()
                val strokeCenter = strokeWidth / 2

                drawLine(
                    color = borderColor,
                    start = Offset(0F, strokeCenter),
                    end = Offset(size.width, strokeCenter),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = borderColor,
                    start = Offset(0F, size.height - strokeCenter),
                    end = Offset(size.width, size.height - strokeCenter),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = borderColor,
                    start = Offset(size.width - strokeCenter, 0F),
                    end = Offset(size.width - strokeCenter, size.height),
                    strokeWidth = strokeWidth
                )
                if (isStartBorderVisible) {
                    drawLine(
                        color = borderColor,
                        start = Offset(strokeCenter, 0F),
                        end = Offset(strokeCenter, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
            .clickable(onClick = onClick)
    ) {
        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            Icon(
                imageVector = Cancel14,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
                .width(96.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClientAsyncImage(
                imageUrl = product.imageUrl,
                modifier = Modifier.size(width = 62.dp, height = 96.dp),
                contentScale = ContentScale.Fit
            )

            BrandBox(
                entity = BrandEntity(
                    brand = product.brand,
                    urlBrandLogo = product.urlBrandLogo
                ),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(width = 91.dp, height = 20.dp),
                style = MaterialTheme.typography.livretMedium18
            )

            Text(
                text = product.price,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(96.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.regular11.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 14.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartAlternativeProductCardPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    CartAlternativeProductCard(
        product = product
    )
}
