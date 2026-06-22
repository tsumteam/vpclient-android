package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.ui.icons.Delete24
import ru.mercury.vpclient.shared.ui.icons.Disassemble24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.surface2

@Composable
fun CartLookCard(
    lookName: String,
    lookImageUrl: String?,
    products: List<CartProduct>,
    onAddClick: () -> Unit,
    onProductClick: (CartProduct) -> Unit,
    onSelectSizeClick: (CartProduct) -> Unit,
    onSizeClick: (CartProduct, CartProductSize) -> Unit = { _, _ -> },
    onBuySwitchChange: (CartProduct, Boolean) -> Unit,
    onAlternativeClick: (CartProductAlternative) -> Unit,
    onRemoveAlternativeClick: (CartProductAlternative) -> Unit,
    onHideAlternativesClick: (CartProduct) -> Unit,
    onEditProductSwipeClick: (CartProduct) -> Unit = {},
    onDeleteProductSwipeClick: (CartProduct) -> Unit = {},
    onDetachProductFromLookSwipeClick: (CartProduct) -> Unit = {},
    onReturnOriginalSwipeClick: (CartProduct) -> Unit = {},
    onShowAlternativesSwipeClick: (CartProduct) -> Unit = {},
    onHideAlternativesSwipeClick: (CartProduct) -> Unit = {},
    onReturnToBasketSwipeClick: (CartProduct) -> Unit = {},
    useFittingProductSwipeActions: Boolean = false,
    onDisassembleLookSwipeClick: () -> Unit = {},
    onDeleteLookSwipeClick: () -> Unit = {},
    productModifier: (CartProduct) -> Modifier = { Modifier },
    selectedAlternativeId: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp), clip = true)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        CartProductSwipeableCard(
            trailingActionsContent = { swipeProgress, onSwipeActionClick ->
                val actionWidth = 88.dp * swipeProgress

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(actionWidth)
                        .clipToBounds(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CartProductSwipeAction(
                        imageVector = Disassemble24,
                        text = stringResource(ClientStrings.CartDisassembleLook),
                        backgroundColor = MaterialTheme.colorScheme.surface2,
                        onClick = { onSwipeActionClick(onDisassembleLookSwipeClick) }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(actionWidth)
                        .clipToBounds(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CartProductSwipeAction(
                        imageVector = Delete24,
                        text = stringResource(ClientStrings.CartDelete),
                        backgroundColor = MaterialTheme.colorScheme.error,
                        onClick = { onSwipeActionClick(onDeleteLookSwipeClick) }
                    )
                }
            },
            trailingSwipeSize = 176.dp
        ) {
            CartLookHeader(
                name = lookName,
                imageUrl = lookImageUrl,
                onAddClick = onAddClick
            )
        }

        products.forEachIndexed { index, product ->
            val isAlternativesVisible = product.isAlternativesPaletteOpen && product.alternatives.isNotEmpty()

            CartProductCard(
                state = CartProductCardState(
                    product = product,
                    onClick = { onProductClick(product) },
                    onSelectSizeClick = { onSelectSizeClick(product) },
                    onSizeClick = { size -> onSizeClick(product, size) },
                    onBuySwitchChange = { paySwitch -> onBuySwitchChange(product, paySwitch) },
                    onAlternativeClick = onAlternativeClick,
                    onRemoveAlternativeClick = onRemoveAlternativeClick,
                    onHideAlternativesClick = { onHideAlternativesClick(product) },
                    onEditSwipeClick = { onEditProductSwipeClick(product) },
                    onDeleteSwipeClick = { onDeleteProductSwipeClick(product) },
                    onDetachFromLookSwipeClick = { onDetachProductFromLookSwipeClick(product) },
                    onReturnOriginalSwipeClick = { onReturnOriginalSwipeClick(product) },
                    onShowAlternativesSwipeClick = { onShowAlternativesSwipeClick(product) },
                    onHideAlternativesSwipeClick = { onHideAlternativesSwipeClick(product) },
                    onReturnToBasketSwipeClick = { onReturnToBasketSwipeClick(product) },
                    useFittingSwipeActions = useFittingProductSwipeActions,
                    selectedAlternativeId = selectedAlternativeId
                ),
                modifier = productModifier(product)
            )

            if (index < products.lastIndex && !isAlternativesVisible) {
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
private fun CartLookCardPreview() {
    CartLookCard(
        lookName = "Evening look",
        lookImageUrl = null,
        products = CartLookCardCartProductProvider().values.take(2).toList(),
        onAddClick = {},
        onProductClick = {},
        onSelectSizeClick = {},
        onBuySwitchChange = { _, _ -> },
        onAlternativeClick = {},
        onRemoveAlternativeClick = {},
        onHideAlternativesClick = {}
    )
}

private class CartLookCardCartProductProvider: PreviewParameterProvider<CartProduct> {
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
            priceValue = 1_600_000.0
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
            priceValue = 300_000.0
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
