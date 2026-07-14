package ru.mercury.vpclient.shared.ui.components.compilations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitDiscountPercentText
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitDiscountPriceText
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitFullPriceText
import ru.mercury.vpclient.shared.domain.mapper.isCompilationBenefitDiscountVisible
import ru.mercury.vpclient.shared.ui.components.product.ProductBrandBox
import ru.mercury.vpclient.shared.ui.components.product.ProductDiscountBadge
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CompilationBenefitProductRow(
    entity: CatalogFilterProductsEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ClientAsyncImage(
            imageUrl = entity.imageUrl,
            modifier = Modifier.size(width = 64.dp, height = 88.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ProductBrandBox(
                entity = BrandEntity(
                    brand = entity.brand,
                    urlBrandLogo = entity.urlBrandLogo
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
            )

            Text(
                text = entity.name,
                maxLines = 1,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = entity.colorId,
                maxLines = 1,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = stringResource(ClientStrings.CartArticle, entity.itemId),
                maxLines = 1,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = entity.compilationBenefitFullPriceText,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.End,
                    textDecoration = if (entity.isCompilationBenefitDiscountVisible) TextDecoration.LineThrough else TextDecoration.None
                )
            )

            if (entity.isCompilationBenefitDiscountVisible) {
                Text(
                    text = entity.compilationBenefitDiscountPriceText,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.End
                    )
                )

                ProductDiscountBadge(
                    percentText = entity.compilationBenefitDiscountPercentText
                )
            }
        }
    }
}
