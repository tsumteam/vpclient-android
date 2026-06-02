@file:OptIn(ExperimentalFoundationApi::class)

package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.shared.data.FORMAT_QUANTITY
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.domain.mapper.imagePages
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.HorizontalPagerIndicator
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.blue
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CartProductLargeCard(
    product: CartProduct,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onSelectSizeClick: () -> Unit = {},
    onSizeClick: (CartProductSize) -> Unit = {},
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
    onReturnToBasketSwipeClick: () -> Unit = {},
    useFittingSwipeActions: Boolean = false,
    selectedAlternativeId: String? = null,
    isDividerVisible: Boolean = true
) {
    val articleText = product.article.takeIf { it.isNotEmpty() } ?: product.itemId
    val dateReceipt = product.dateReceipt?.takeIf { it.isNotBlank() }
    val isDateReceiptBadgeVisible = dateReceipt != null
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
    val leadingSwipeActionsCount = when {
        useFittingSwipeActions -> 0
        else -> {
            listOf(
                isReturnOriginalSwipeActionVisible,
                isShowAlternativesSwipeActionVisible,
                isHideAlternativesSwipeActionVisible
            ).count { it }
        }
    }
    val trailingSwipeActionsCount = when {
        useFittingSwipeActions -> 2
        else -> {
            listOf(
                isEditSwipeActionVisible,
                isDetachFromLookSwipeActionVisible,
                true
            ).count { it }
        }
    }

    CartProductSwipeableCard(
        modifier = modifier,
        leadingActionsContent = { swipeProgress, onSwipeActionClick ->
            CartProductLeadingSwipeActions(
                swipeProgress = swipeProgress,
                isReturnOriginalVisible = isReturnOriginalSwipeActionVisible,
                isShowAlternativesVisible = isShowAlternativesSwipeActionVisible,
                isHideAlternativesVisible = isHideAlternativesSwipeActionVisible,
                onReturnOriginalClick = { onSwipeActionClick(onReturnOriginalSwipeClick) },
                onShowAlternativesClick = { onSwipeActionClick(onShowAlternativesSwipeClick) },
                onHideAlternativesClick = { onSwipeActionClick(onHideAlternativesSwipeClick) }
            )
        },
        trailingActionsContent = { swipeProgress, onSwipeActionClick ->
            when {
                useFittingSwipeActions -> {
                    CartProductFittingSwipeActions(
                        swipeProgress = swipeProgress,
                        onEditClick = { onSwipeActionClick(onEditSwipeClick) },
                        onReturnToBasketClick = { onSwipeActionClick(onReturnToBasketSwipeClick) }
                    )
                }
                else -> {
                    CartProductTrailingSwipeActions(
                        swipeProgress = swipeProgress,
                        isEditVisible = isEditSwipeActionVisible,
                        isDetachFromLookVisible = isDetachFromLookSwipeActionVisible,
                        isDeleteVisible = true,
                        onEditClick = { onSwipeActionClick(onEditSwipeClick) },
                        onDetachFromLookClick = { onSwipeActionClick(onDetachFromLookSwipeClick) },
                        onDeleteClick = { onSwipeActionClick(onDeleteSwipeClick) }
                    )
                }
            }
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
                    buyRow,
                    dateReceiptBadge
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
                        inactiveColor = MaterialTheme.colorScheme.outlineVariant,
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

                if (dateReceipt != null) {
                    Box(
                        modifier = Modifier.constrainAs(dateReceiptBadge) {
                            start.linkTo(title.start)
                            top.linkTo(price.bottom, 10.dp)
                        }
                            .height(20.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(4.dp),
                                clip = false
                            )
                            .background(
                                color = when {
                                    product.isDateReceiptOverdue -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.blue
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Выкупить до $dateReceipt",
                            style = MaterialTheme.typography.regular11.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 9.sp,
                                lineHeight = 12.sp,
                                letterSpacing = 0.sp
                            )
                        )
                    }
                }

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
                    text = FORMAT_QUANTITY.format(product.quantity),
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
                            top.linkTo(
                                anchor = when {
                                    isDateReceiptBadgeVisible -> dateReceiptBadge.bottom
                                    else -> price.bottom
                                },
                                margin = when {
                                    isDateReceiptBadgeVisible -> 10.dp
                                    else -> 5.dp
                                }
                            )
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = .2.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                    !product.isSold && !hasSize -> {
                        OutlinedButton(
                            onClick = onSelectSizeClick,
                            modifier = modifier.constrainAs(size) {
                                width = Dimension.wrapContent
                                height = Dimension.value(32.dp)
                                top.linkTo(
                                    anchor = when {
                                        isDateReceiptBadgeVisible -> dateReceiptBadge.bottom
                                        else -> price.bottom
                                    },
                                    margin = when {
                                        isDateReceiptBadgeVisible -> 10.dp
                                        else -> 5.dp
                                    }
                                )
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(ClientStrings.CartSelectSize),
                                style = MaterialTheme.typography.regular15.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    lineHeight = 19.sp,
                                    letterSpacing = .2.sp
                                )
                            )
                        }
                    }
                    else -> {
                        when {
                            product.sizeItems.size > 1 -> {
                                Column(
                                    modifier = Modifier.constrainAs(size) {
                                        width = Dimension.wrapContent
                                        height = Dimension.wrapContent
                                        top.linkTo(
                                            anchor = when {
                                                isDateReceiptBadgeVisible -> dateReceiptBadge.bottom
                                                else -> price.bottom
                                            },
                                            margin = when {
                                                isDateReceiptBadgeVisible -> 10.dp
                                                else -> 5.dp
                                            }
                                        )
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    product.sizeItems.forEach { item ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .height(24.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = MaterialTheme.colorScheme.outlineVariant,
                                                        shape = RoundedCornerShape(6.dp)
                                                    )
                                                    .padding(start = 8.dp, end = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = item.name,
                                                    style = MaterialTheme.typography.regular11.copy(
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        lineHeight = 16.sp,
                                                        letterSpacing = .2.sp
                                                    )
                                                )

                                                IconButton(
                                                    onClick = { onSizeClick(item) },
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

                                            if (item.isLastInStock) {
                                                Text(
                                                    text = stringResource(ClientStrings.CartInStock),
                                                    modifier = Modifier.padding(top = 1.dp),
                                                    style = MaterialTheme.typography.regular11.copy(
                                                        color = MaterialTheme.colorScheme.error,
                                                        lineHeight = 16.sp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {
                                Text(
                                    text = product.size,
                                    modifier = Modifier.constrainAs(size) {
                                        width = Dimension.wrapContent
                                        height = Dimension.wrapContent
                                        top.linkTo(
                                            anchor = when {
                                                isDateReceiptBadgeVisible -> dateReceiptBadge.bottom
                                                else -> price.bottom
                                            },
                                            margin = when {
                                                isDateReceiptBadgeVisible -> 10.dp
                                                else -> 5.dp
                                            }
                                        )
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.regular14.copy(
                                        color = MaterialTheme.colorScheme.outline,
                                        letterSpacing = .2.sp,
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }

                if (isAvailabilityVisible && product.sizeItems.size <= 1) {
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
    @PreviewParameter(CartProductLargeCardCartProductProvider::class) product: CartProduct
) {
    CartProductLargeCard(
        product = product
    )
}

private class CartProductLargeCardCartProductProvider: PreviewParameterProvider<CartProduct> {
    override val values: Sequence<CartProduct> = previewCartProducts()
}

private fun previewCartProducts(): Sequence<CartProduct> {
    return sequenceOf(
        CartProduct(
            id = "1",
            detailId = "1",
            itemId = "1",
            colorId = "1",
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            name = "Хлопковая футболка с логотипом",
            article = "MP827743",
            color = "Белый",
            size = "IT 48",
            price = "1 600 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            isForPayment = true,
            priceValue = 1_600_000.0,
            dateReceipt = "23 июня",
            isDateReceiptOverdue = false
        ),
        CartProduct(
            id = "2",
            detailId = "2",
            itemId = "2",
            colorId = "2",
            brand = "SAINT LAURENT",
            urlBrandLogo = null,
            name = "Кожаная куртка",
            article = "SL908221",
            color = "Черный",
            size = "FR 38",
            price = "300 000 ₽",
            oldPrice = "400 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            isForPayment = false,
            quantity = 2,
            priceValue = 300_000.0,
            dateReceipt = "23 июня",
            isDateReceiptOverdue = true
        ),
        CartProduct(
            id = "3",
            detailId = "3",
            itemId = "3",
            colorId = "3",
            brand = "LORO PIANA",
            urlBrandLogo = null,
            name = "Кашемировый джемпер",
            article = "LP112490",
            color = "Серый",
            size = "M",
            price = "580 000 ₽",
            imageUrl = "",
            isForPayment = false,
            isSold = true,
            isAlternativesPaletteOpen = true,
            alternatives = listOf(
                CartProductAlternative(
                    id = "1",
                    detailId = "1",
                    brand = "LORO PIANA",
                    urlBrandLogo = null,
                    price = "580 000 ₽",
                    imageUrl = "",
                    isOriginal = true
                ),
                CartProductAlternative(
                    id = "2",
                    detailId = "2",
                    brand = "DOLCE&GABBANA",
                    urlBrandLogo = null,
                    price = "1 900 000 ₽",
                    imageUrl = "",
                    isOriginal = false
                )
            ),
            priceValue = 580_000.0
        ),
        CartProduct(
            id = "4",
            detailId = "4",
            itemId = "4",
            colorId = "4",
            brand = "KITON",
            urlBrandLogo = null,
            name = "Шерстяной жакет",
            article = "KT554210",
            color = "Темно-синий",
            size = "",
            price = "920 000 ₽",
            imageUrl = "",
            isForPayment = false,
            priceValue = 920_000.0
        )
    )
}
