package ru.mercury.vpclient.core.ui.components.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.core.ktx.cardDiscountLabel
import ru.mercury.vpclient.core.ktx.imagePages
import ru.mercury.vpclient.core.ktx.isDiscountLabelVisible
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.core.ui.components.BrandBox
import ru.mercury.vpclient.core.ui.components.DiscountBadge
import ru.mercury.vpclient.core.ui.components.HorizontalPagerIndicator
import ru.mercury.vpclient.core.ui.components.PriceText
import ru.mercury.vpclient.core.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Message24
import ru.mercury.vpclient.core.ui.preview.CatalogFilterProductsEntityProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular14

@Composable
fun CatalogProductCard(
    entity: CatalogFilterProductsEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onBasketClick: () -> Unit = {}
) {
    val pagerImages = remember(entity.imagePages) { entity.imagePages }
    val pagerState = rememberPagerState(pageCount = { pagerImages.size })

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(388.dp)
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
    ) {
        val (messageButton, basketButton, pager, pagerIndicator, brand, title, price, discountBadge) = createRefs()

        IconButton(
            onClick = onMessageClick,
            modifier = Modifier.constrainAs(messageButton) {
                start.linkTo(parent.start, 2.dp)
                top.linkTo(parent.top, 2.dp)
            }
        ) {
            Icon(
                imageVector = Message24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

        IconButton(
            onClick = onBasketClick,
            modifier = Modifier.constrainAs(basketButton) {
                top.linkTo(parent.top, 2.dp)
                end.linkTo(parent.end, 2.dp)
            }
        ) {
            Icon(
                imageVector = Basket24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.constrainAs(pager) {
                width = Dimension.fillToConstraints
                height = Dimension.value(216.dp)
                start.linkTo(parent.start, 12.dp)
                top.linkTo(messageButton.bottom)
                end.linkTo(parent.end, 12.dp)
            },
            userScrollEnabled = pagerImages.size > 1
        ) { page ->
            ClientAsyncImage(
                imageUrl = pagerImages[page],
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = pagerImages.size,
            modifier = Modifier.constrainAs(pagerIndicator) {
                start.linkTo(parent.start)
                top.linkTo(pager.bottom, 2.dp)
                end.linkTo(parent.end)
            }
        )

        BrandBox(
            brand = entity.brand,
            urlBrandLogo = entity.urlBrandLogo,
            modifier = Modifier.constrainAs(brand) {
                start.linkTo(parent.start)
                top.linkTo(pagerIndicator.bottom, 3.dp)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = entity.name,
            modifier = Modifier.constrainAs(title) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 16.dp)
                top.linkTo(brand.bottom, 1.dp)
                end.linkTo(parent.end, 16.dp)
            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )

        PriceText(
            entity = entity,
            modifier = Modifier.constrainAs(price) {
                start.linkTo(parent.start)
                top.linkTo(title.bottom, 2.dp)
                end.linkTo(parent.end)
            }
        )

        if (entity.isDiscountLabelVisible) {
            DiscountBadge(
                percentText = entity.cardDiscountLabel.orEmpty(),
                modifier = Modifier.constrainAs(discountBadge) {
                    start.linkTo(parent.start)
                    top.linkTo(price.bottom, 2.dp)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@FontScalePreviews
@Composable
private fun CatalogProductCardPreview(
    @PreviewParameter(CatalogFilterProductsEntityProvider::class) product: CatalogFilterProductsEntity
) {
    ClientTheme {
        CatalogProductCard(
            entity = product,
            modifier = Modifier.width(200.dp)
        )
    }
}
