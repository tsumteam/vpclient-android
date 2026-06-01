@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_fitting_products_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart_fitting_products_sheet.intent.CartFittingProductsSheetIntent
import ru.mercury.vpclient.features.cart_fitting_products_sheet.model.CartFittingProductsSheetModel
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.cart.CartAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartFittingProductsSheet(
    state: CartFittingProductsSheetModel,
    dispatch: (CartFittingProductsSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var selectedProductIds by remember(state.products) { mutableStateOf(emptySet<String>()) }

    val sheetDispatch: (CartFittingProductsSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CartFittingProductsSheetIntent.ProductCheckedChange -> {
                selectedProductIds = when {
                    intent.checked -> selectedProductIds + intent.productId
                    else -> selectedProductIds - intent.productId
                }
            }
            is CartFittingProductsSheetIntent.ConfirmClick -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is CartFittingProductsSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartFittingProductsSheetIntent.DismissRequest) },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        sheetState = sheetState
    ) {
        CartFittingProductsSheetContent(
            state = state.copy(selectedProductIds = selectedProductIds),
            dispatch = sheetDispatch
        )
    }
}

@Composable
private fun CartFittingProductsSheetContent(
    state: CartFittingProductsSheetModel,
    dispatch: (CartFittingProductsSheetIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CartFittingProductsSheetTitle),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(CartFittingProductsSheetIntent.DismissRequest) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            Button(
                onClick = { dispatch(CartFittingProductsSheetIntent.ConfirmClick(state.selectedProductIds.toList())) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.selectedProductIds.isNotEmpty(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.disabled,
                    disabledContentColor = MaterialTheme.colorScheme.onDisabled
                )
            ) {
                Text(
                    text = when {
                        state.selectedProductIds.isEmpty() -> stringResource(ClientStrings.CartFittingProductsSheetSelect)
                        else -> stringResource(ClientStrings.CartFittingSheetConfirm)
                    },
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                itemsIndexed(
                    items = state.products,
                    key = { _, product -> product.id }
                ) { index, product ->
                    CartFittingProductRow(
                        product = product,
                        checked = state.selectedProductIds.contains(product.id),
                        onCheckedChange = { checked ->
                            dispatch(CartFittingProductsSheetIntent.ProductCheckedChange(product.id, checked))
                        }
                    )

                    if (index < state.products.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartFittingProductRow(
    product: CartProduct,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .clickable { onCheckedChange(!checked) }
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        CartAsyncImage(
            imageUrl = product.imageUrl,
            modifier = Modifier.size(width = 64.dp, height = 96.dp)
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = product.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Column(
            modifier = Modifier.width(112.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = product.price,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = product.size,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingProductsSheetContentPreview() {
    val products = CartFittingProductsSheetCartProductProvider().values.toList().filter { it.size.isNotBlank() && !it.isSold }

    CartFittingProductsSheetContent(
        state = CartFittingProductsSheetModel(
            products = products,
            selectedProductIds = setOf("1")
        ),
        dispatch = {}
    )
}

private class CartFittingProductsSheetCartProductProvider: PreviewParameterProvider<CartProduct> {
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
