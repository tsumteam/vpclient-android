@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilation_add_to_basket_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.intent.CompilationAddToBasketSheetIntent
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.model.CompilationAddToBasketSheetModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.CompilationPreviewBasketProductCard
import ru.mercury.vpclient.shared.ui.components.CompilationPreviewBasketProductCardState
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled

@Composable
fun CompilationAddToBasketSheet(
    state: CompilationAddToBasketSheetModel,
    dispatch: (CompilationAddToBasketSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CompilationAddToBasketSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CompilationAddToBasketSheetIntent.AddToBasketClick,
            is CompilationAddToBasketSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is CompilationAddToBasketSheetIntent.AddToBasketProductCheckedChange -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { sheetDispatch(CompilationAddToBasketSheetIntent.DismissRequest) },
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
                            text = stringResource(ClientStrings.CompilationPreviewAddToBasketDescription),
                            style = MaterialTheme.typography.livretMedium18.copy(
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { sheetDispatch(CompilationAddToBasketSheetIntent.DismissRequest) }
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null
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
                ) {
                    Button(
                        onClick = { sheetDispatch(CompilationAddToBasketSheetIntent.AddToBasketClick) },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = state.isAddToBasketButtonEnabled,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.disabled,
                            disabledContentColor = MaterialTheme.colorScheme.onDisabled
                        )
                    ) {
                        Text(
                            text = stringResource(ClientStrings.DetailsAddToBasket),
                            style = MaterialTheme.typography.medium15.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = .3.sp
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding + PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = state.productEntities,
                    key = { entity -> entity.id }
                ) { entity ->
                    CompilationPreviewBasketProductCard(
                        state = CompilationPreviewBasketProductCardState(
                            entity = entity,
                            checked = entity.id in state.selectedProductIds,
                            onCheckedChange = { checked ->
                                sheetDispatch(CompilationAddToBasketSheetIntent.AddToBasketProductCheckedChange(entity.id, checked))
                            },
                            sizeSelectorState = state.sizeSelectorState(entity)
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
private fun CompilationAddToBasketSheetPreview(
    @PreviewParameter(CompilationAddToBasketSheetStateProvider::class) state: CompilationAddToBasketSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CompilationAddToBasketSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CompilationAddToBasketSheetStateProvider: PreviewParameterProvider<CompilationAddToBasketSheetModel> {

    private val products = listOf(
        CatalogFilterProductsEntity.Empty.copy(
            id = "preview-1",
            itemId = "5558447",
            colorId = "black",
            brand = "SAINT LAURENT",
            name = "Куртка из кожи",
            price = 129_900.0,
            imageUrl = ""
        ),
        CatalogFilterProductsEntity.Empty.copy(
            id = "preview-2",
            itemId = "5558448",
            colorId = "white",
            brand = "BRUNELLO CUCINELLI",
            name = "Платье миди с поясом",
            price = 189_900.0,
            imageUrl = ""
        )
    )

    override val values: Sequence<CompilationAddToBasketSheetModel> = sequenceOf(
        CompilationAddToBasketSheetModel(
            productEntities = products,
            selectedProductIds = products.map { it.id }.toSet(),
            isLoading = false
        ),
        CompilationAddToBasketSheetModel(
            productEntities = emptyList(),
            selectedProductIds = emptySet(),
            isLoading = false
        )
    )
}
