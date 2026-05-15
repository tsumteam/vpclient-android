package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.secondary6

@Composable
fun CartListProductCard(
    product: CartProduct,
    onClick: () -> Unit = {},
    onSelectSizeClick: () -> Unit = {},
    onBuySwitchChange: (Boolean) -> Unit = {},
    onAlternativeClick: (CartProductAlternative) -> Unit = {},
    onRemoveAlternativeClick: (CartProductAlternative) -> Unit = {},
    onHideAlternativesClick: () -> Unit = {}
) {
    val articleText = product.article.takeIf { it.isNotEmpty() } ?: product.itemId
    val isAvailabilityVisible = !product.isSold && product.size.isNotBlank() && product.isLastInStock
    val isAlternativesVisible = product.isAlternativesPaletteOpen && product.alternatives.isNotEmpty()
    val isAlternativesEmptyVisible = product.isAlternativesPaletteOpen && product.alternatives.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                quantity
            ) = createRefs()

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
                maxLines = 1,
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
                    end.linkTo(buySwitch.start, 8.dp)
                    width = Dimension.fillToConstraints
                },
                maxLines = 1,
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
                    bottom.linkTo(image.bottom, 4.dp)
                    end.linkTo(quantity.start, 8.dp)
                    width = Dimension.fillToConstraints
                }
            )

            CartBuySwitch(
                checked = product.isForPayment && !product.isSold,
                modifier = Modifier.constrainAs(buySwitch) {
                    top.linkTo(image.top)
                    end.linkTo(parent.end, 16.dp)
                },
                isVisible = !product.isSold,
                onCheckedChange = onBuySwitchChange
            )

            if (product.isSold) {
                Text(
                    text = stringResource(ClientStrings.CartSold),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    ),
                    modifier = Modifier.constrainAs(sold) {
                        top.linkTo(image.top, 4.dp)
                        end.linkTo(parent.end, 16.dp)
                    }
                )
            }

            when {
                product.isSold -> {
                    if (product.size.isNotBlank()) {
                        Text(
                            text = product.size,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.secondary6,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp
                            ),
                            modifier = Modifier.constrainAs(size) {
                                top.linkTo(sold.bottom, 4.dp)
                                end.linkTo(sold.end)
                            }
                        )
                    }
                }
                product.size.isBlank() -> {
                    CartSelectSizeButton(
                        onClick = onSelectSizeClick,
                        modifier = Modifier.constrainAs(size) {
                            top.linkTo(buySwitch.bottom, 21.dp)
                            end.linkTo(buySwitch.end)
                        }
                    )
                }
                else -> {
                    Text(
                        text = product.size,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        ),
                        modifier = Modifier.constrainAs(size) {
                            top.linkTo(buySwitch.bottom, 21.dp)
                            end.linkTo(buySwitch.end)
                        }
                    )
                }
            }

            if (isAvailabilityVisible) {
                Text(
                    text = stringResource(ClientStrings.CartInStock),
                    style = MaterialTheme.typography.regular11.copy(
                        color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.constrainAs(availability) {
                        top.linkTo(size.bottom, 1.dp)
                        end.linkTo(size.end)
                    }
                )
            }

            Text(
                text = "х ${product.quantity}",
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                ),
                modifier = Modifier.constrainAs(quantity) {
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(price.bottom)
                }
            )
        }

        if (isAlternativesVisible) {
            CartAlternativesSection(
                alternatives = product.alternatives,
                onAlternativeClick = onAlternativeClick,
                onRemoveClick = onRemoveAlternativeClick
            )
        }

        if (isAlternativesEmptyVisible) {
            CartAlternativesEmpty(
                onHideClick = onHideAlternativesClick
            )
        }

        if (!isAlternativesVisible && !isAlternativesEmptyVisible) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.divider,
                thickness = 1.dp
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartListProductCardPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    CartListProductCard(
        product = product
    )
}
