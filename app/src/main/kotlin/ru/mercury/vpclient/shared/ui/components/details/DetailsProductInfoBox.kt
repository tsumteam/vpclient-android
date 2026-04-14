package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ktx.cardDiscountLabel
import ru.mercury.vpclient.shared.ktx.isDiscountLabelVisible
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.DiscountBadge
import ru.mercury.vpclient.shared.ui.components.PriceText
import ru.mercury.vpclient.shared.ui.icons.Message24
import ru.mercury.vpclient.shared.ui.preview.ProductInfoBoxProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun DetailsProductInfoBox(
    productEntity: ProductEntity,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        IconButton(
            onClick = onMessageClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 31.dp)
        ) {
            Icon(
                imageVector = Message24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

        BrandBox(
            brand = productEntity.brand.orEmpty(),
            urlBrandLogo = productEntity.urlBrandLogo,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 180.dp, height = 50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 64.dp, top = 54.dp, end = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = productEntity.shortDescription.orEmpty(),
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.regular14.copy(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
            )

            productEntity.cashboxActions.forEach { action ->
                Text(
                    text = action,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.regular14.copy(color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                )
            }

            PriceText(
                entity = productEntity,
                modifier = Modifier.padding(top = 6.dp)
            )

            if (productEntity.isDiscountLabelVisible) {
                DiscountBadge(
                    percentText = productEntity.cardDiscountLabel.orEmpty(),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun DetailsProductInfoBoxPreview(
    @PreviewParameter(ProductInfoBoxProvider::class) productEntity: ProductEntity
) {
    ClientTheme {
        DetailsProductInfoBox(
            productEntity = productEntity,
            onMessageClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
