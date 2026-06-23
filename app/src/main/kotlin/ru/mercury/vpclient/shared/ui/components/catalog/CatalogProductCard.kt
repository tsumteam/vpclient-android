package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.cardDiscountLabel
import ru.mercury.vpclient.shared.domain.mapper.imagePages
import ru.mercury.vpclient.shared.domain.mapper.isDiscountLabelVisible
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.DiscountBadge
import ru.mercury.vpclient.shared.ui.components.SharedHorizontalPagerIndicator
import ru.mercury.vpclient.shared.ui.components.PriceText
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Basket24
import ru.mercury.vpclient.shared.ui.icons.BasketFilled24
import ru.mercury.vpclient.shared.ui.icons.Message24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular14
import kotlin.time.Duration.Companion.milliseconds

private const val ADDED_TO_BASKET_BADGE_VISIBLE_DURATION = 1_500L
private const val ADDED_TO_BASKET_BADGE_FADE_OUT_DURATION = 800L

data class CatalogProductCardState(
    val entity: CatalogFilterProductsEntity,
    val isInBasket: Boolean = false
)

@Composable
fun CatalogProductCard(
    state: CatalogProductCardState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onBasketClick: () -> Unit = {}
) {
    val entity = state.entity
    val pagerImages = remember(entity.imagePages) { entity.imagePages }
    val pagerState = rememberPagerState(
        pageCount = { if (pagerImages.isEmpty()) 0 else Int.MAX_VALUE }
    )
    var isAddedToBasketBadgeVisible by remember { mutableStateOf(false) }
    var addedToBasketBadgeTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(pagerImages.size) {
        if (pagerImages.isNotEmpty()) {
            val mid = Int.MAX_VALUE / 2
            pagerState.scrollToPage(mid - mid % pagerImages.size)
        }
    }

    LaunchedEffect(addedToBasketBadgeTrigger) {
        if (addedToBasketBadgeTrigger == 0) return@LaunchedEffect
        isAddedToBasketBadgeVisible = true
        delay(ADDED_TO_BASKET_BADGE_VISIBLE_DURATION.milliseconds)
        isAddedToBasketBadgeVisible = false
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(388.dp)
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
    ) {
        val (
            messageButton,
            basketButton,
            addedToBasketBadge,
            pager,
            pagerIndicator,
            brand,
            title,
            price,
            discountBadge
        ) = createRefs()

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
            onClick = {
                if (!state.isInBasket) {
                    addedToBasketBadgeTrigger += 1
                }
                onBasketClick()
            },
            modifier = Modifier.constrainAs(basketButton) {
                top.linkTo(parent.top, 2.dp)
                end.linkTo(parent.end, 2.dp)
            }
        ) {
            Icon(
                imageVector = if (state.isInBasket) BasketFilled24 else Basket24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = when {
                    state.isInBasket -> MaterialTheme.colorScheme.onBackground
                    else -> Color.Unspecified
                }
            )
        }

        AnimatedVisibility(
            visible = isAddedToBasketBadgeVisible,
            enter = EnterTransition.None,
            exit = fadeOut(
                animationSpec = tween(durationMillis = ADDED_TO_BASKET_BADGE_FADE_OUT_DURATION.toInt())
            ),
            modifier = Modifier.constrainAs(addedToBasketBadge) {
                top.linkTo(basketButton.top)
                end.linkTo(basketButton.start)
                bottom.linkTo(basketButton.bottom)
            }
        ) {
            Box(
                modifier = Modifier
                    .height(28.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(start = 8.dp, end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(ClientStrings.CatalogAddedToCart),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.regular12.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 16.sp
                    )
                )
            }
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
                imageUrl = pagerImages[page % pagerImages.size],
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        SharedHorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = pagerImages.size,
            pageIndexMapping = { it % pagerImages.size },
            modifier = Modifier.constrainAs(pagerIndicator) {
                start.linkTo(parent.start)
                top.linkTo(pager.bottom, 2.dp)
                end.linkTo(parent.end)
            }
        )

        BrandBox(
            entity = BrandEntity(
                brand = entity.brand,
                urlBrandLogo = entity.urlBrandLogo
            ),
            modifier = Modifier.constrainAs(brand) {
                start.linkTo(parent.start)
                top.linkTo(pagerIndicator.bottom, 3.dp)
                end.linkTo(parent.end)
            }
        )

        Row(
            modifier = Modifier.constrainAs(title) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 16.dp)
                top.linkTo(brand.bottom, 1.dp)
                end.linkTo(parent.end, 16.dp)
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entity.name,
                modifier = Modifier.weight(1F, fill = false),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )

            if (entity.additionalColorPhotoUrls.isNotEmpty()) {
                CatalogProductColorsRow(
                    colorPhotoUrls = entity.additionalColorPhotoUrls
                )
            }
        }

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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CatalogProductCardPreview(
    @PreviewParameter(CatalogProductCardCatalogFilterProductsEntityProvider::class) product: CatalogFilterProductsEntity
) {
    CatalogProductCard(
        state = CatalogProductCardState(
            entity = product
        ),
        modifier = Modifier.width(200.dp)
    )
}

private class CatalogProductCardCatalogFilterProductsEntityProvider: PreviewParameterProvider<CatalogFilterProductsEntity> {
    override val values: Sequence<CatalogFilterProductsEntity> = sequenceOf(
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 0,
            id = "preview-1",
            itemId = "item-1",
            colorId = "black",
            name = "Кожаная куртка oversize",
            price = 189_900.0,
            priceWithoutDiscount = 234_900.0,
            brand = "SAINT LAURENT",
            urlBrandLogo = "https://example.com/brand-logo.png",
            imageUrl = "",
            imageUrls = listOf("", ""),
            additionalColorPhotoUrls = listOf(
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Black.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Grey.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Green.png"
            )
        ),
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 1,
            id = "preview-2",
            itemId = "item-2",
            colorId = "white",
            name = "Хлопковая футболка с логотипом",
            price = 32_700.0,
            priceWithoutDiscount = null,
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = listOf(""),
            additionalColorPhotoUrls = emptyList()
        )
    )
}
