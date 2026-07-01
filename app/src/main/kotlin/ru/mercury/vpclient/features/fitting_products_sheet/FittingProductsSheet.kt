@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_products_sheet

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.fitting_products_sheet.intent.FittingProductsSheetIntent
import ru.mercury.vpclient.features.fitting_products_sheet.model.FittingProductsSheetModel
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
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
fun FittingProductsSheet(
    viewModel: FittingProductsSheetViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    FittingProductsSheetContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun FittingProductsSheetContent(
    state: FittingProductsSheetModel,
    dispatch: (FittingProductsSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (FittingProductsSheetIntent) -> Unit = { intent ->
        when (intent) {
            is FittingProductsSheetIntent.ConfirmClick -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is FittingProductsSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is FittingProductsSheetIntent.CollectProducts -> dispatch(intent)
            is FittingProductsSheetIntent.ProductCheckedChange -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(FittingProductsSheetIntent.DismissRequest) },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        sheetState = sheetState
    ) {
        SharedScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.FittingProductsSheetTitle),
                            style = MaterialTheme.typography.livretMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { sheetDispatch(FittingProductsSheetIntent.DismissRequest) }
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
                        onClick = { sheetDispatch(FittingProductsSheetIntent.ConfirmClick) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = state.isConfirmEnabled,
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
                                state.isConfirmEnabled -> stringResource(ClientStrings.CartFittingSheetConfirm)
                                else -> stringResource(ClientStrings.FittingProductsSheetSelect)
                            },
                            style = MaterialTheme.typography.medium15.copy(
                                letterSpacing = .3.sp
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = innerPadding + PaddingValues(bottom = 8.dp)
            ) {
                itemsIndexed(
                    items = state.cartProductEntities,
                    key = { _, product -> product.id }
                ) { index, product ->
                    FittingProductRow(
                        state = FittingProductRowState(
                            cartProductEntity = product,
                            checked = state.selectedProductIds.contains(product.id),
                            onCheckedChange = { checked -> sheetDispatch(FittingProductsSheetIntent.ProductCheckedChange(product.id, checked)) }
                        )
                    )

                    if (index < state.cartProductEntities.lastIndex) {
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
private fun FittingProductsSheetPreview(
    @PreviewParameter(FittingProductsSheetModelProvider::class) state: FittingProductsSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FittingProductsSheetContent(
            state = state,
            dispatch = {}
        )
    }
}

private class FittingProductsSheetModelProvider: PreviewParameterProvider<FittingProductsSheetModel> {
    private val products = listOf(
        CartProductEntity(
            id = "1",
            position = 0,
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
            oldPrice = null,
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            imageUrls = emptyList(),
            isForPayment = true,
            isSold = false,
            isLastInStock = false,
            hasActions = false,
            isAlternativesPaletteOpen = false,
            isAlternativePaletteControlsAvailable = false,
            isSwitchAlternativeBackToOriginalAvailable = false,
            alternatives = emptyList(),
            discountPercentage = 0,
            quantity = 1,
            sizeCount = 1,
            sizeId = "48",
            sizeItems = emptyList(),
            priceValue = 1_600_000.0
        ),
        CartProductEntity(
            id = "2",
            position = 1,
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
            imageUrls = emptyList(),
            isForPayment = false,
            isSold = false,
            isLastInStock = false,
            hasActions = false,
            isAlternativesPaletteOpen = false,
            isAlternativePaletteControlsAvailable = false,
            isSwitchAlternativeBackToOriginalAvailable = false,
            alternatives = emptyList(),
            discountPercentage = 0,
            quantity = 2,
            sizeCount = 1,
            sizeId = "38",
            sizeItems = emptyList(),
            priceValue = 300_000.0
        ),
        CartProductEntity(
            id = "3",
            position = 2,
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
            oldPrice = null,
            lookId = null,
            lookName = null,
            lookImageUrl = null,
            imageUrl = "",
            imageUrls = emptyList(),
            isForPayment = false,
            isSold = true,
            isLastInStock = false,
            hasActions = false,
            isAlternativesPaletteOpen = true,
            isAlternativePaletteControlsAvailable = false,
            isSwitchAlternativeBackToOriginalAvailable = false,
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
            discountPercentage = 0,
            quantity = 1,
            sizeCount = 1,
            sizeId = "m",
            sizeItems = emptyList(),
            priceValue = 580_000.0
        ),
        CartProductEntity(
            id = "4",
            position = 3,
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
            oldPrice = null,
            lookId = null,
            lookName = null,
            lookImageUrl = null,
            imageUrl = "",
            imageUrls = emptyList(),
            isForPayment = false,
            isSold = false,
            isLastInStock = false,
            hasActions = false,
            isAlternativesPaletteOpen = false,
            isAlternativePaletteControlsAvailable = false,
            isSwitchAlternativeBackToOriginalAvailable = false,
            alternatives = emptyList(),
            discountPercentage = 0,
            quantity = 1,
            sizeCount = 1,
            sizeId = "",
            sizeItems = emptyList(),
            priceValue = 920_000.0
        )
    )

    override val values: Sequence<FittingProductsSheetModel> = sequenceOf(
        FittingProductsSheetModel(
            cartProductEntities = products.filter { it.size.isNotBlank() && !it.isSold },
            selectedProductIds = setOf("1")
        )
    )
}
