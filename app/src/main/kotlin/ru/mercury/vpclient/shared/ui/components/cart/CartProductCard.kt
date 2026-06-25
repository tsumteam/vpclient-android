package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15

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
    val swipeKey: String? = null,
    val openedSwipeKey: String? = null,
    val onSwipeOpen: (String?) -> Unit = {},
    val useFittingSwipeActions: Boolean = false,
    val selectedAlternativeId: String? = null
) {
    val articleText: String
        get() = product.article.takeIf { it.isNotEmpty() } ?: product.itemId

    val isPriceVisible: Boolean
        get() = product.priceValue > .0 && product.price.isNotBlank()

    val dateReceipt: String?
        get() = product.dateReceipt?.takeIf { it.isNotBlank() }

    val dateReceiptBadgeText: String
        get() = dateReceipt.orEmpty()

    val isDateReceiptBadgeVisible: Boolean
        get() = dateReceipt != null

    val hasSize: Boolean
        get() = product.size.isNotBlank()

    val isSoldSizeVisible: Boolean
        get() = product.isSold && hasSize

    val isSelectSizeButtonVisible: Boolean
        get() = !product.isSold && !hasSize

    val isMultipleSizesVisible: Boolean
        get() = product.sizeItems.size > 1

    val isAvailabilityVisible: Boolean
        get() = !product.isSold && hasSize && product.isLastInStock

    val isSingleSizeAvailabilityVisible: Boolean
        get() = isAvailabilityVisible && product.sizeItems.size <= 1

    val isMultipleSizesAvailabilityVisible: Boolean
        get() = isMultipleSizesVisible && product.sizeItems.any { it.isLastInStock }

    val isAlternativesVisible: Boolean
        get() = product.isAlternativesPaletteOpen && product.alternatives.isNotEmpty()

    val isAlternativesEmptyVisible: Boolean
        get() = product.isAlternativesPaletteOpen && product.alternatives.isEmpty()

    val isReturnOriginalSwipeActionVisible: Boolean
        get() = product.isSwitchAlternativeBackToOriginalAvailable || product.isAlternativeSelected

    val isShowAlternativesSwipeActionVisible: Boolean
        get() = product.isAlternativePaletteControlsAvailable && !product.isAlternativesPaletteOpen

    val isHideAlternativesSwipeActionVisible: Boolean
        get() = product.isAlternativePaletteControlsAvailable && product.isAlternativesPaletteOpen

    val isEditSwipeActionVisible: Boolean
        get() = hasSize && product.sizeCount in 1..2

    val isDetachFromLookSwipeActionVisible: Boolean
        get() = !product.lookId.isNullOrEmpty()

    val quantityText: String
        get() = FORMAT_QUANTITY.format(product.quantity)

    val leadingSwipeActionsCount: Int
        get() = when {
            useFittingSwipeActions -> 0
            else -> {
                listOf(
                    isReturnOriginalSwipeActionVisible,
                    isShowAlternativesSwipeActionVisible,
                    isHideAlternativesSwipeActionVisible
                ).count { it }
            }
        }

    val trailingSwipeActionsCount: Int
        get() = when {
            useFittingSwipeActions -> 2
            else -> {
                listOf(
                    isEditSwipeActionVisible,
                    isDetachFromLookSwipeActionVisible,
                    true
                ).count { it }
            }
        }
}

@Composable
fun CartProductCard(
    state: CartProductCardState,
    modifier: Modifier = Modifier,
) {
    val product = state.product

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CartProductSwipeableCard(
            leadingActionsContent = { swipeProgress, onSwipeActionClick ->
                CartProductLeadingSwipeActions(
                    swipeProgress = swipeProgress,
                    isReturnOriginalVisible = state.isReturnOriginalSwipeActionVisible,
                    isShowAlternativesVisible = state.isShowAlternativesSwipeActionVisible,
                    isHideAlternativesVisible = state.isHideAlternativesSwipeActionVisible,
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
                            isEditVisible = state.isEditSwipeActionVisible,
                            isDetachFromLookVisible = state.isDetachFromLookSwipeActionVisible,
                            isDeleteVisible = true,
                            onEditClick = { onSwipeActionClick(state.onEditSwipeClick) },
                            onDetachFromLookClick = { onSwipeActionClick(state.onDetachFromLookSwipeClick) },
                            onDeleteClick = { onSwipeActionClick(state.onDeleteSwipeClick) }
                        )
                    }
                }
            },
            leadingSwipeSize = (88 * state.leadingSwipeActionsCount).dp,
            trailingSwipeSize = (88 * state.trailingSwipeActionsCount).dp,
            swipeKey = state.swipeKey,
            openedSwipeKey = state.openedSwipeKey,
            onSwipeOpen = state.onSwipeOpen
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 178.dp)
                    .clickable(onClick = state.onClick)
            ) {
                val (
                    image,
                    header,
                    title,
                    color,
                    article,
                    price,
                    size,
                    availability,
                    dateReceiptBadge
                ) = createRefs()
                val mainContentBottom = createGuidelineFromTop(178.dp)

                ClientAsyncImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier.constrainAs(image) {
                        width = Dimension.value(85.dp)
                        height = Dimension.value(130.dp)
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top, 24.dp)
                    },
                    contentScale = ContentScale.Fit
                )

                Row(
                    modifier = Modifier.constrainAs(header) {
                        width = Dimension.fillToConstraints
                        start.linkTo(image.end, 16.dp)
                        top.linkTo(image.top)
                        end.linkTo(parent.end, 16.dp)
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CartBrandBox(
                        entity = BrandEntity(
                            brand = product.brand,
                            urlBrandLogo = product.urlBrandLogo
                        ),
                        modifier = Modifier
                            .height(24.dp)
                            .weight(1F)
                    )

                    when {
                        product.isSold -> {
                            Text(
                                text = stringResource(ClientStrings.CartSold),
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    lineHeight = 18.sp,
                                    letterSpacing = .2.sp
                                )
                            )
                        }
                        else -> {
                            CartBuySwitch(
                                checked = product.isForPayment,
                                onCheckedChange = state.onBuySwitchChange
                            )
                        }
                    }
                }

                Text(
                    text = product.name,
                    modifier = Modifier.constrainAs(title) {
                        width = Dimension.fillToConstraints
                        start.linkTo(header.start)
                        top.linkTo(header.bottom)
                        end.linkTo(parent.end, 16.dp)
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
                        width = Dimension.fillToConstraints
                        start.linkTo(header.start)
                        top.linkTo(title.bottom, 4.dp)
                        end.linkTo(size.start, 8.dp)
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
                    text = stringResource(ClientStrings.CartArticle, state.articleText),
                    modifier = Modifier.constrainAs(article) {
                        width = Dimension.fillToConstraints
                        start.linkTo(header.start)
                        top.linkTo(color.bottom, 4.dp)
                        end.linkTo(size.start, 8.dp)
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Row(
                    modifier = Modifier.constrainAs(price) {
                        start.linkTo(header.start)
                        top.linkTo(
                            anchor = when {
                                state.isMultipleSizesAvailabilityVisible -> size.bottom
                                else -> article.bottom
                            },
                            margin = when {
                                state.isMultipleSizesAvailabilityVisible -> 8.dp
                                state.isDateReceiptBadgeVisible -> 3.dp
                                else -> 17.dp
                            }
                        )
                        end.linkTo(parent.end, 16.dp)
                        bottom.linkTo(
                            anchor = when {
                                state.isPriceVisible -> mainContentBottom
                                else -> image.bottom
                            },
                            margin = when {
                                state.isPriceVisible && state.isDateReceiptBadgeVisible -> 57.dp
                                state.isPriceVisible -> 27.dp
                                else -> 3.dp
                            }
                        )
                        verticalBias = 0F
                        width = Dimension.fillToConstraints
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CartPriceRow(
                        product = product,
                        modifier = Modifier.weight(1F)
                    )

                    Text(
                        text = state.quantityText,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }

                if (state.isDateReceiptBadgeVisible) {
                    CartProductDateReceiptBadge(
                        state = CartProductDateReceiptBadgeState(
                            text = stringResource(ClientStrings.CartRedeemUntil, state.dateReceiptBadgeText),
                            isOverdue = product.isDateReceiptOverdue
                        ),
                        modifier = Modifier.constrainAs(dateReceiptBadge) {
                            start.linkTo(header.start)
                            top.linkTo(price.bottom, 10.dp)
                        }
                    )
                }

                when {
                    state.isSoldSizeVisible -> {
                        Text(
                            text = product.size,
                            modifier = Modifier.constrainAs(size) {
                                top.linkTo(color.top)
                                end.linkTo(parent.end, 16.dp)
                            },
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                    state.isSelectSizeButtonVisible -> {
                        OutlinedButton(
                            onClick = state.onSelectSizeClick,
                            modifier = Modifier.constrainAs(size) {
                                width = Dimension.wrapContent
                                height = Dimension.value(32.dp)
                                top.linkTo(color.top)
                                end.linkTo(parent.end, 16.dp)
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
                            state.isMultipleSizesVisible -> {
                                Column(
                                    modifier = Modifier.constrainAs(size) {
                                        top.linkTo(color.top)
                                        end.linkTo(parent.end, 16.dp)
                                    },
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    product.sizeItems.forEach { item ->
                                        Column(
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            CartProductSizeChip(
                                                size = item,
                                                onRemoveClick = { state.onSizeClick(item) }
                                            )

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
                                        top.linkTo(color.top)
                                        end.linkTo(parent.end, 16.dp)
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

                if (state.isSingleSizeAvailabilityVisible) {
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

            }
        }

        if (state.isAlternativesVisible) {
            CartAlternativesSection(
                state = CartAlternativesSectionState(
                    alternatives = product.alternatives,
                    selectedAlternativeId = state.selectedAlternativeId
                ),
                onAlternativeClick = state.onAlternativeClick,
                onRemoveClick = state.onRemoveAlternativeClick
            )
        }

        if (state.isAlternativesEmptyVisible) {
            CartAlternativesEmpty(
                modifier = Modifier.fillMaxWidth(),
                onHideClick = state.onHideAlternativesClick
            )
        }
    }
}

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
