@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.details_wear_with_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.details_wear_with_sheet.intent.DetailsWearWithSheetIntent
import ru.mercury.vpclient.features.details_wear_with_sheet.model.DetailsWearWithSheetModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.product.ProductCard
import ru.mercury.vpclient.shared.ui.components.product.ProductCardState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

@Composable
fun DetailsWearWithSheet(
    state: DetailsWearWithSheetModel,
    dispatch: (DetailsWearWithSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (DetailsWearWithSheetIntent) -> Unit = { intent ->
        when (intent) {
            is DetailsWearWithSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is DetailsWearWithSheetIntent.ProductBasketClick,
            is DetailsWearWithSheetIntent.ProductClick -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(DetailsWearWithSheetIntent.DismissRequest) },
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
                            text = stringResource(ClientStrings.DetailsWearWithTitle),
                            style = MaterialTheme.typography.livretMedium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { sheetDispatch(DetailsWearWithSheetIntent.DismissRequest) }
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
            }
        ) { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding + PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = state.products,
                    key = { product -> product.id }
                ) { product ->
                    ProductCard(
                        state = ProductCardState(
                            entity = product,
                            isInBasket = state.isProductInBasket(product),
                            onClick = { sheetDispatch(DetailsWearWithSheetIntent.ProductClick(product.id)) },
                            onBasketIconClick = { sheetDispatch(DetailsWearWithSheetIntent.ProductBasketClick(product)) }
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun DetailsWearWithSheetPreview(
    @PreviewParameter(DetailsWearWithSheetModelProvider::class) state: DetailsWearWithSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DetailsWearWithSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class DetailsWearWithSheetModelProvider: PreviewParameterProvider<DetailsWearWithSheetModel> {
    override val values: Sequence<DetailsWearWithSheetModel> = sequenceOf(
        DetailsWearWithSheetModel(
            products = listOf(
                CatalogFilterProductsEntity.Empty.copy(
                    id = "preview-1",
                    itemId = "12345",
                    colorId = "black",
                    name = "Кожаная куртка oversize",
                    price = 189_900.0,
                    priceWithoutDiscount = 234_900.0,
                    brand = "SAINT LAURENT"
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "preview-2",
                    itemId = "67890",
                    colorId = "grey",
                    name = "Брюки прямого кроя",
                    price = 98_900.0,
                    priceWithoutDiscount = null,
                    brand = "LORO PIANA"
                )
            ),
            basketProductIds = setOf("preview-1"),
            basketProductKeys = emptySet()
        )
    )
}
