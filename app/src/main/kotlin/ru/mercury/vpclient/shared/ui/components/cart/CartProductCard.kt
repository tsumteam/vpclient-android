package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
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
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.blue
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CartProductCard(
    state: CartProductCardState,
    modifier: Modifier = Modifier,
) {
    val product = state.product
    val articleText = product.article.takeIf { it.isNotEmpty() } ?: product.itemId
    val isPriceVisible = product.priceValue > .0 && product.price.isNotBlank()
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
        state.useFittingSwipeActions -> 0
        else -> {
            listOf(
                isReturnOriginalSwipeActionVisible,
                isShowAlternativesSwipeActionVisible,
                isHideAlternativesSwipeActionVisible
            ).count { it }
        }
    }
    val trailingSwipeActionsCount = when {
        state.useFittingSwipeActions -> 2
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
                onReturnOriginalClick = { onSwipeActionClick(state.onReturnOriginalSwipeClick) },
                onShowAlternativesClick = { onSwipeActionClick(state.onShowAlternativesSwipeClick) },
                onHideAlternativesClick = { onSwipeActionClick(state.onHideAlternativesSwipeClick) }
            )
        },
        trailingActionsContent = { swipeProgress, onSwipeActionClick ->
            when {
                state.useFittingSwipeActions -> {
                    CartProductFittingSwipeActions(
                        swipeProgress = swipeProgress,
                        onEditClick = { onSwipeActionClick(state.onEditSwipeClick) },
                        onReturnToBasketClick = { onSwipeActionClick(state.onReturnToBasketSwipeClick) }
                    )
                }
                else -> {
                    CartProductTrailingSwipeActions(
                        swipeProgress = swipeProgress,
                        isEditVisible = isEditSwipeActionVisible,
                        isDetachFromLookVisible = isDetachFromLookSwipeActionVisible,
                        isDeleteVisible = true,
                        onEditClick = { onSwipeActionClick(state.onEditSwipeClick) },
                        onDetachFromLookClick = { onSwipeActionClick(state.onDetachFromLookSwipeClick) },
                        onDeleteClick = { onSwipeActionClick(state.onDeleteSwipeClick) }
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
                .clickable(onClick = state.onClick)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 178.dp)
            ) {
                val (
                    image,
                    brand,
                    title,
                    color,
                    article,
                    price,
                    sold,
                    buySwitch,
                    size,
                    availability,
                    quantity,
                    dateReceiptBadge
                ) = createRefs()
                val priceTopBarrier = createBottomBarrier(article, size)

                ClientAsyncImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier
                        .size(width = 85.dp, height = 130.dp)
                        .constrainAs(image) {
                            start.linkTo(parent.start, 16.dp)
                            top.linkTo(parent.top, 24.dp)
                        },
                    contentScale = ContentScale.Fit
                )

                CartBrandBox(
                    entity = BrandEntity(
                        brand = product.brand,
                        urlBrandLogo = product.urlBrandLogo
                    ),
                    modifier = Modifier
                        .width(119.dp)
                        .height(24.dp)
                        .constrainAs(brand) {
                            start.linkTo(image.end, 16.dp)
                            top.linkTo(image.top, 4.dp)
                        }
                )

                Text(
                    text = product.name,
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(brand.start)
                        top.linkTo(brand.bottom, 4.dp)
                        end.linkTo(buySwitch.start, 8.dp)
                        width = Dimension.fillToConstraints
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Text(
                    text = product.color,
                    modifier = Modifier.constrainAs(color) {
                        start.linkTo(brand.start)
                        top.linkTo(title.bottom, 4.dp)
                        end.linkTo(buySwitch.start, 8.dp)
                        width = Dimension.fillToConstraints
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Text(
                    text = stringResource(ClientStrings.CartArticle, articleText),
                    modifier = Modifier.constrainAs(article) {
                        start.linkTo(brand.start)
                        top.linkTo(color.bottom, 4.dp)
                        end.linkTo(size.start, 8.dp)
                        width = Dimension.fillToConstraints
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                CartPriceRow(
                    product = product,
                    modifier = Modifier.constrainAs(price) {
                        start.linkTo(brand.start)
                        top.linkTo(priceTopBarrier, 5.dp)
                        end.linkTo(quantity.start, 8.dp)
                        if (isPriceVisible) {
                            bottom.linkTo(parent.bottom, if (isDateReceiptBadgeVisible) 57.dp else 27.dp)
                            verticalBias = 0F
                        }
                        width = Dimension.fillToConstraints
                    }
                )

                if (dateReceipt != null) {
                    Box(
                        modifier = Modifier.constrainAs(dateReceiptBadge) {
                            start.linkTo(brand.start)
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
                            text = stringResource(ClientStrings.CartRedeemUntil, dateReceipt),
                            style = MaterialTheme.typography.regular11.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 9.sp,
                                lineHeight = 12.sp,
                                letterSpacing = 0.sp
                            )
                        )
                    }
                }

                CartBuySwitch(
                    checked = product.isForPayment && !product.isSold,
                    modifier = Modifier.constrainAs(buySwitch) {
                        top.linkTo(image.top)
                        end.linkTo(parent.end, 16.dp)
                    },
                    isVisible = !product.isSold,
                    onCheckedChange = state.onBuySwitchChange
                )

                if (product.isSold) {
                    Text(
                        text = stringResource(ClientStrings.CartSold),
                        modifier = Modifier.constrainAs(sold) {
                            top.linkTo(image.top, 4.dp)
                            end.linkTo(parent.end, 16.dp)
                        },
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }

                when {
                    product.isSold && hasSize -> {
                        Text(
                            text = product.size,
                            modifier = Modifier.constrainAs(size) {
                                top.linkTo(sold.bottom, 4.dp)
                                end.linkTo(sold.end)
                            },
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                    !product.isSold && !hasSize -> {
                        OutlinedButton(
                            onClick = state.onSelectSizeClick,
                            modifier = modifier.constrainAs(size) {
                                width = Dimension.wrapContent
                                height = Dimension.value(32.dp)
                                top.linkTo(buySwitch.bottom, 21.dp)
                                end.linkTo(buySwitch.end)
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
                                        top.linkTo(buySwitch.bottom, 21.dp)
                                        end.linkTo(buySwitch.end)
                                    },
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    product.sizeItems.forEach { item ->
                                        Column(
                                            horizontalAlignment = Alignment.End
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
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                                                    onClick = { state.onSizeClick(item) },
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
                                                        color = MaterialTheme.colorScheme.error
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
                                        top.linkTo(buySwitch.bottom, 21.dp)
                                        end.linkTo(buySwitch.end)
                                    },
                                    style = MaterialTheme.typography.regular14.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        lineHeight = 18.sp,
                                        letterSpacing = .2.sp
                                    )
                                )
                            }
                        }
                    }
                }

                if (isAvailabilityVisible && product.sizeItems.size <= 1) {
                    Text(
                        text = stringResource(ClientStrings.CartInStock),
                        modifier = Modifier.constrainAs(availability) {
                            top.linkTo(size.bottom, 1.dp)
                            end.linkTo(size.end)
                        },
                        style = MaterialTheme.typography.regular11.copy(
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                }

                Text(
                    text = FORMAT_QUANTITY.format(product.quantity),
                    modifier = Modifier.constrainAs(quantity) {
                        end.linkTo(parent.end, 16.dp)
                        when {
                            isAvailabilityVisible && product.sizeItems.size <= 1 -> {
                                top.linkTo(availability.bottom, 8.dp)
                            }
                            else -> {
                                top.linkTo(price.top)
                            }
                        }
                        bottom.linkTo(
                            anchor = when {
                                isPriceVisible -> price.bottom
                                else -> image.bottom
                            },
                            margin = when {
                                isPriceVisible -> 0.dp
                                else -> 3.dp
                            }
                        )
                    },
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )
            }

            if (isAlternativesVisible) {
                CartAlternativesSection(
                    alternatives = product.alternatives,
                    selectedAlternativeId = state.selectedAlternativeId,
                    onAlternativeClick = state.onAlternativeClick,
                    onRemoveClick = state.onRemoveAlternativeClick
                )
            }

            if (isAlternativesEmptyVisible) {
                CartAlternativesEmpty(
                    onHideClick = state.onHideAlternativesClick
                )
            }
        }
    }
}

data class CartProductCardState(
    val product: CartProduct,
    val onClick: () -> Unit = {},
    val onSelectSizeClick: () -> Unit = {},
    val onSizeClick: (CartProductSize) -> Unit = {},
    val onBuySwitchChange: (Boolean) -> Unit = {},
    val onAlternativeClick: (CartProductAlternative) -> Unit = {},
    val onRemoveAlternativeClick: (CartProductAlternative) -> Unit = {},
    val onHideAlternativesClick: () -> Unit = {},
    val onEditSwipeClick: () -> Unit = {},
    val onDeleteSwipeClick: () -> Unit = {},
    val onDetachFromLookSwipeClick: () -> Unit = {},
    val onReturnOriginalSwipeClick: () -> Unit = {},
    val onShowAlternativesSwipeClick: () -> Unit = {},
    val onHideAlternativesSwipeClick: () -> Unit = {},
    val onReturnToBasketSwipeClick: () -> Unit = {},
    val useFittingSwipeActions: Boolean = false,
    val selectedAlternativeId: String? = null
)

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductCardPreview(
    @PreviewParameter(CartProductCardCartProductProvider::class) product: CartProduct
) {
    CartProductCard(
        state = CartProductCardState(
            product = product
        )
    )
}

private class CartProductCardCartProductProvider: PreviewParameterProvider<CartProduct> {
    override val values: Sequence<CartProduct> = sequenceOf(
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
        ),
        CartProduct(
            id = "5",
            detailId = "5",
            itemId = "5",
            colorId = "5",
            brand = "PRADA",
            urlBrandLogo = null,
            name = "Хлопковая рубашка",
            article = "PR112233",
            color = "Молочный",
            size = "IT 38, IT 40",
            price = "210 000 ₽",
            imageUrl = "",
            isForPayment = true,
            isLastInStock = true,
            sizeCount = 2,
            sizeItems = listOf(
                CartProductSize(
                    id = "38",
                    name = "IT 38",
                    productId = "5_38",
                    isLastInStock = true
                ),
                CartProductSize(
                    id = "40",
                    name = "IT 40",
                    productId = "5_40"
                )
            ),
            priceValue = 210_000.0
        )
    )
}
