@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_fitting_products_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
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
import ru.mercury.vpclient.shared.ui.components.fitting.FittingProductRow
import ru.mercury.vpclient.shared.ui.components.fitting.FittingProductRowState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled

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
        val inlinedState = state.copy(selectedProductIds = selectedProductIds)

        SharedScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.CartFittingProductsSheetTitle),
                            style = MaterialTheme.typography.livretMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { sheetDispatch(CartFittingProductsSheetIntent.DismissRequest) }
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Button(
                        onClick = { sheetDispatch(CartFittingProductsSheetIntent.ConfirmClick(inlinedState.selectedProductIds.toList())) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = inlinedState.selectedProductIds.isNotEmpty(),
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
                                inlinedState.selectedProductIds.isEmpty() -> stringResource(ClientStrings.CartFittingProductsSheetSelect)
                                else -> stringResource(ClientStrings.CartFittingSheetConfirm)
                            },
                            style = MaterialTheme.typography.medium15.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = .3.sp
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = innerPadding + PaddingValues(bottom = 8.dp)
            ) {
                itemsIndexed(
                    items = inlinedState.products,
                    key = { _, product -> product.id }
                ) { index, product ->
                    FittingProductRow(
                        state = FittingProductRowState(
                            product = product,
                            checked = inlinedState.selectedProductIds.contains(product.id)
                        ),
                        onCheckedChange = { checked ->
                            sheetDispatch(CartFittingProductsSheetIntent.ProductCheckedChange(product.id, checked))
                        }
                    )

                    if (index < inlinedState.products.lastIndex) {
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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartFittingProductsSheetPreview(
    @PreviewParameter(CartFittingProductsSheetModelProvider::class) state: CartFittingProductsSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartFittingProductsSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CartFittingProductsSheetModelProvider: PreviewParameterProvider<CartFittingProductsSheetModel> {
    private val products = listOf(
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

    override val values: Sequence<CartFittingProductsSheetModel> = sequenceOf(
        CartFittingProductsSheetModel(
            products = products.filter { it.size.isNotBlank() && !it.isSold },
            selectedProductIds = setOf("1")
        )
    )
}
