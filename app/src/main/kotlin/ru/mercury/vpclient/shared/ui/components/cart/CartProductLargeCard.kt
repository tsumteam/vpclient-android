@file:OptIn(ExperimentalFoundationApi::class)

package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.domain.mapper.imagePages
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.HorizontalPagerIndicator
import ru.mercury.vpclient.shared.ui.components.SharedOutlinedButton2
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15
import ru.mercury.vpclient.shared.ui.theme.secondary4
import ru.mercury.vpclient.shared.ui.theme.secondary5
import ru.mercury.vpclient.shared.ui.theme.secondary6

@Composable
fun CartProductLargeCard(
    product: CartProduct,
    onClick: () -> Unit = {},
    onSelectSizeClick: () -> Unit = {},
    onBuySwitchChange: (Boolean) -> Unit = {},
    onAlternativeClick: (CartProductAlternative) -> Unit = {},
    onRemoveAlternativeClick: (CartProductAlternative) -> Unit = {},
    onHideAlternativesClick: () -> Unit = {},
    onEditSwipeClick: () -> Unit = {},
    onDeleteSwipeClick: () -> Unit = {},
    onDetachFromLookSwipeClick: () -> Unit = {},
    onReturnOriginalSwipeClick: () -> Unit = {},
    onShowAlternativesSwipeClick: () -> Unit = {},
    onHideAlternativesSwipeClick: () -> Unit = {},
    selectedAlternativeId: String? = null,
    isDividerVisible: Boolean = true
) {
    val articleText = product.article.takeIf { it.isNotEmpty() } ?: product.itemId
    val hasSize = product.size.isNotBlank()
    val isAvailabilityVisible = !product.isSold && hasSize && product.isLastInStock
    val isAlternativesVisible = product.isAlternativesPaletteOpen && product.alternatives.isNotEmpty()
    val isAlternativesEmptyVisible = product.isAlternativesPaletteOpen && product.alternatives.isEmpty()
    val isReturnOriginalSwipeActionVisible = product.isSwitchAlternativeBackToOriginalAvailable
    val isShowAlternativesSwipeActionVisible = product.isAlternativePaletteControlsAvailable &&
        !product.isAlternativesPaletteOpen
    val isHideAlternativesSwipeActionVisible = product.isAlternativePaletteControlsAvailable &&
        product.isAlternativesPaletteOpen
    val isEditSwipeActionVisible = hasSize && product.sizeCount in 1..2
    val isDetachFromLookSwipeActionVisible = !product.lookId.isNullOrEmpty()
    val leadingSwipeActionsCount = listOf(
        isReturnOriginalSwipeActionVisible,
        isShowAlternativesSwipeActionVisible,
        isHideAlternativesSwipeActionVisible
    ).count { it }
    val trailingSwipeActionsCount = listOf(
        isEditSwipeActionVisible,
        isDetachFromLookSwipeActionVisible,
        true
    ).count { it }

    CartProductSwipeableCard(
        leadingActionsContent = { swipeProgress ->
            CartProductLeadingSwipeActions(
                swipeProgress = swipeProgress,
                isReturnOriginalVisible = isReturnOriginalSwipeActionVisible,
                isShowAlternativesVisible = isShowAlternativesSwipeActionVisible,
                isHideAlternativesVisible = isHideAlternativesSwipeActionVisible,
                onReturnOriginalClick = onReturnOriginalSwipeClick,
                onShowAlternativesClick = onShowAlternativesSwipeClick,
                onHideAlternativesClick = onHideAlternativesSwipeClick
            )
        },
        trailingActionsContent = { swipeProgress ->
            CartProductTrailingSwipeActions(
                swipeProgress = swipeProgress,
                isEditVisible = isEditSwipeActionVisible,
                isDetachFromLookVisible = isDetachFromLookSwipeActionVisible,
                isDeleteVisible = true,
                onEditClick = onEditSwipeClick,
                onDetachFromLookClick = onDetachFromLookSwipeClick,
                onDeleteClick = onDeleteSwipeClick
            )
        },
        leadingSwipeSize = (88 * leadingSwipeActionsCount).dp,
        trailingSwipeSize = (88 * trailingSwipeActionsCount).dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 412.dp)
            ) {
                val (
                    pager,
                    indicator,
                    brand,
                    title,
                    color,
                    price,
                    article,
                    quantity,
                    sold,
                    size,
                    availability,
                    buyRow
                ) = createRefs()

                val pagerImages = remember(product.imagePages) { product.imagePages }
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { Int.MAX_VALUE }
                )
                val configuration = LocalConfiguration.current
                val titleMaxWidth = (configuration.screenWidthDp / 2).dp - 24.dp

                LaunchedEffect(pagerImages.size) {
                    val mid = Int.MAX_VALUE / 2
                    pagerState.scrollToPage(mid - mid % pagerImages.size)
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(284.dp)
                        .constrainAs(pager) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top, 16.dp)
                        },
                    userScrollEnabled = pagerImages.size > 1
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ClientAsyncImage(
                            imageUrl = pagerImages[page % pagerImages.size],
                            modifier = Modifier.size(width = 184.dp, height = 284.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Row(
                    modifier = Modifier.constrainAs(indicator) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(pager.bottom)
                        height = Dimension.value(44.dp)
                        width = Dimension.value(184.dp)
                    },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        pageCount = pagerImages.size,
                        pageIndexMapping = { it % pagerImages.size },
                        activeColor = MaterialTheme.colorScheme.onBackground,
                        inactiveColor = MaterialTheme.colorScheme.secondary5,
                        indicatorWidth = 6.dp,
                        indicatorHeight = 6.dp,
                        spacing = 10.dp
                    )
                }

                BrandBox(
                    entity = BrandEntity(
                        brand = product.brand,
                        urlBrandLogo = product.urlBrandLogo
                    ),
                    modifier = Modifier
                        .size(width = 134.dp, height = 28.dp)
                        .constrainAs(brand) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(indicator.bottom)
                        }
                )

                Text(
                    text = product.name,
                    modifier = Modifier
                        .widthIn(max = titleMaxWidth)
                        .constrainAs(title) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            start.linkTo(parent.start, 24.dp)
                            top.linkTo(brand.bottom, 14.dp)
                        },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp,
                        lineHeight = 18.sp
                    )
                )

                Text(
                    text = product.color,
                    modifier = Modifier.constrainAs(color) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        start.linkTo(title.start)
                        top.linkTo(title.bottom, 4.dp)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp,
                        lineHeight = 18.sp
                    )
                )

                CartPriceRow(
                    product = product,
                    modifier = Modifier.constrainAs(price) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        start.linkTo(title.start)
                        top.linkTo(color.bottom, 8.dp)
                    }
                )

                Text(
                    text = stringResource(ClientStrings.CartArticle, articleText),
                    modifier = Modifier.constrainAs(article) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        top.linkTo(title.top)
                        end.linkTo(parent.end, 16.dp)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp,
                        lineHeight = 18.sp
                    )
                )

                Text(
                    text = "х ${product.quantity}",
                    modifier = Modifier.constrainAs(quantity) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        top.linkTo(article.bottom, 4.dp)
                        end.linkTo(parent.end, 16.dp)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp,
                        lineHeight = 18.sp
                    )
                )

                if (product.isSold) {
                    Text(
                        text = stringResource(ClientStrings.CartSold),
                        modifier = Modifier.constrainAs(sold) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            top.linkTo(price.bottom, 5.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.error,
                            letterSpacing = .2.sp,
                            lineHeight = 18.sp
                        )
                    )
                }

                when {
                    product.isSold && hasSize -> {
                        Text(
                            text = product.size,
                            modifier = Modifier.constrainAs(size) {
                                width = Dimension.wrapContent
                                height = Dimension.wrapContent
                                top.linkTo(sold.bottom, 8.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.secondary6,
                                letterSpacing = .2.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                    !product.isSold && !hasSize -> {
                        SharedOutlinedButton2(
                            onClick = onSelectSizeClick,
                            text = stringResource(ClientStrings.CartSelectSize),
                            textStyle = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            ),
                            modifier = Modifier.constrainAs(size) {
                                width = Dimension.wrapContent
                                height = Dimension.wrapContent
                                top.linkTo(price.bottom, 5.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )
                    }
                    else -> {
                        Text(
                            text = product.size,
                            modifier = Modifier.constrainAs(size) {
                                width = Dimension.wrapContent
                                height = Dimension.wrapContent
                                top.linkTo(price.bottom, 5.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.secondary4,
                                letterSpacing = .2.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                if (isAvailabilityVisible) {
                    Text(
                        text = stringResource(ClientStrings.CartInStock),
                        style = MaterialTheme.typography.regular11.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp
                        ),
                        modifier = Modifier.constrainAs(availability) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            top.linkTo(size.bottom, 1.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }

                if (!product.isSold) {
                    CartBuySwitch(
                        checked = product.isForPayment,
                        modifier = Modifier.constrainAs(buyRow) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            top.linkTo(
                                anchor = when {
                                    isAvailabilityVisible -> availability.bottom
                                    else -> size.bottom
                                },
                                margin = when {
                                    isAvailabilityVisible -> 8.dp
                                    else -> 10.dp
                                }
                            )
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, 14.dp)
                        },
                        onCheckedChange = onBuySwitchChange
                    )
                }
            }

            if (isAlternativesVisible) {
                CartAlternativesSection(
                    alternatives = product.alternatives,
                    modifier = Modifier.padding(top = 25.dp),
                    selectedAlternativeId = selectedAlternativeId,
                    onAlternativeClick = onAlternativeClick,
                    onRemoveClick = onRemoveAlternativeClick
                )
            }

            if (isAlternativesEmptyVisible) {
                CartAlternativesEmpty(
                    onHideClick = onHideAlternativesClick
                )
            }

            if (isDividerVisible && !isAlternativesVisible) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductLargeCardPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    CartProductLargeCard(
        product = product
    )
}
