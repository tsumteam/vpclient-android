@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.filter_price_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.filter_price_sheet.intent.FilterPriceIntent
import ru.mercury.vpclient.features.filter_price_sheet.model.FilterPriceModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireQuantity
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium16

@Composable
fun FilterPriceSheet(
    state: FilterPriceModel,
    dispatch: (FilterPriceIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(FilterPriceIntent.HideFilterPriceDialog) },
        sheetState = sheetState
    ) {
        FilterPriceSheetContent(
            state = state,
            dispatch = dispatch
        )
    }
}

@Composable
private fun FilterPriceSheetContent(
    state: FilterPriceModel,
    dispatch: (FilterPriceIntent) -> Unit
) {
    val isResetVisible = state.priceFrom.isNotEmpty() || state.priceTo.isNotEmpty() || state.selectedPresetId != null

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
        ) {
            IconButton(
                onClick = { dispatch(FilterPriceIntent.HideFilterPriceDialog) },
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

            SharedAnimatedVisibility(
                visible = isResetVisible,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                TextButton(
                    onClick = { dispatch(FilterPriceIntent.ResetPrice) },
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
                onValueChange = { value -> dispatch(FilterPriceIntent.ChangeMinPrice(value)) },
                placeholder = "From",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1F)
            )

            ClientTextField(
                value = state.priceTo,
                accepted = true,
                onValueChange = { value -> dispatch(FilterPriceIntent.ChangeMaxPrice(value)) },
                placeholder = "To",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            SharedLazyColumn(
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
                        onClick = { dispatch(FilterPriceIntent.SelectPricePreset(item.id)) }
                    )

                    if (index != state.presets.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 48.dp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Button(
                onClick = { dispatch(FilterPriceIntent.ConfirmPrice) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isProductsQuantityLoading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                when {
                    state.isProductsQuantityLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = pluralStringResource(
                                ClientStrings.FilterShowProductsQuantity,
                                state.quantityEntity.requireQuantity,
                                state.quantityEntity.quantityWithThousandsSeparator
                            ),
                            style = MaterialTheme.typography.medium15.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = .3.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FilterPriceSheetPreview(
    @PreviewParameter(FilterPriceModelProvider::class) state: FilterPriceModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        FilterPriceSheetContent(
            state = state,
            dispatch = {}
        )
    }
}

private class FilterPriceModelProvider: PreviewParameterProvider<FilterPriceModel> {
    override val values: Sequence<FilterPriceModel> = sequenceOf(
        FilterPriceModel(
            title = "Price",
            presets = emptyList(),
            selectedPresetId = null,
            priceFrom = "10000",
            priceTo = "50000",
            quantityEntity = FilterValuesQuantityEntity("price", 12),
            isProductsQuantityLoading = false
        ),
        FilterPriceModel(
            title = "Price",
            presets = emptyList(),
            selectedPresetId = null,
            priceFrom = "",
            priceTo = "",
            quantityEntity = FilterValuesQuantityEntity("price", 0),
            isProductsQuantityLoading = true
        )
    )
}
