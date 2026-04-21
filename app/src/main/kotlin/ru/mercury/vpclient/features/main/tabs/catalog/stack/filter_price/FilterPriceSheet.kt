@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_price

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.intent.FilterIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_price.model.FilterPriceSheetState
import ru.mercury.vpclient.shared.domain.mapper.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireQuantity
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientDragHandle
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium16

@Composable
fun FilterPriceSheet(
    state: FilterPriceSheetState,
    dispatch: (FilterIntent) -> Unit
) {
    FilterPriceSheetContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun FilterPriceSheetContent(
    state: FilterPriceSheetState,
    dispatch: (FilterIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val isResetVisible = state.priceFrom.isNotEmpty() || state.priceTo.isNotEmpty() || state.selectedPresetId != null

    ModalBottomSheet(
        onDismissRequest = { dispatch(FilterIntent.HideFilterValuesDialog) },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { ClientDragHandle() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            dispatch(FilterIntent.HideFilterValuesDialog)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Close24,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = state.title.uppercase(),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp),
                    style = MaterialTheme.typography.livretMedium19.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )

                ClientAnimatedVisibility(
                    visible = isResetVisible,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(
                        onClick = { dispatch(FilterIntent.ResetPrice) },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CommonReset),
                            style = MaterialTheme.typography.medium16.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
            ) {
                ClientTextField(
                    value = state.priceFrom,
                    accepted = true,
                    onValueChange = { value -> dispatch(FilterIntent.UpdatePriceFrom(value)) },
                    placeholder = "From",
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1F)
                )

                ClientTextField(
                    value = state.priceTo,
                    accepted = true,
                    onValueChange = { value -> dispatch(FilterIntent.UpdatePriceTo(value)) },
                    placeholder = "To",
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F, fill = false)
            ) {
                ClientLazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 72.dp)
                ) {
                    itemsIndexed(
                        items = state.presets,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        FilterSelectableRow(
                            text = item.label,
                            selected = item.id == state.selectedPresetId,
                            onClick = { dispatch(FilterIntent.SelectPricePreset(item.id)) }
                        )

                        if (index != state.presets.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 48.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                ClientButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            dispatch(FilterIntent.ConfirmPrice)
                        }
                    },
                    text = pluralStringResource(
                        ClientStrings.FilterShowProductsQuantity,
                        state.quantityEntity.requireQuantity,
                        state.quantityEntity.quantityWithThousandsSeparator
                    ),
                    loading = state.isProductsQuantityLoading,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FilterPriceSheetPreview() {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            FilterPriceSheet(
                state = FilterPriceSheetState(
                    title = "Price",
                    presets = emptyList(),
                    selectedPresetId = null,
                    priceFrom = "10000",
                    priceTo = "50000",
                    quantityEntity = ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity("price", 12),
                    isProductsQuantityLoading = false
                ),
                dispatch = {}
            )
        }
    }
}
