@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.filter_size_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.filter_size_sheet.intent.FilterSizeIntent
import ru.mercury.vpclient.features.filter_size_sheet.model.FilterSizeModel
import ru.mercury.vpclient.shared.data.entity.SizeCountry
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireQuantity
import ru.mercury.vpclient.shared.domain.mapper.sizeValues
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.components.filters.FilterSizeCountrySelector
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium16

@Composable
fun FilterSizeSheet(
    state: FilterSizeModel,
    dispatch: (FilterSizeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(FilterSizeIntent.HideFilterSizeDialog) },
        sheetState = sheetState
    ) {
        var selectedCountry by remember { mutableStateOf<SizeCountry>(SizeCountry.Russia) }

        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.FilterSizeTitle),
                        style = MaterialTheme.typography.livretMedium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FilterSizeIntent.HideFilterSizeDialog) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    SharedAnimatedVisibility(
                        visible = state.selectedIds.isNotEmpty()
                    ) {
                        TextButton(
                            onClick = { dispatch(FilterSizeIntent.ResetFilterSizeValues) },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(ClientStrings.CommonReset),
                                style = MaterialTheme.typography.medium16
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.error
                )
            )

            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 16.dp,
                                alignment = Alignment.CenterHorizontally
                            )
                        ) {
                            repeat(4) {
                                Spacer(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .placeholder(shape = CircleShape)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 16.dp)
                                .width(120.dp)
                                .height(19.dp)
                                .placeholder(shape = RoundedCornerShape(6.dp))
                        )

                        repeat(4) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 4.dp)
                                    .height(48.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .placeholder(shape = RoundedCornerShape(8.dp))
                    )
                }
                else -> {
                    val visibleSizeValues = state.entity.sizeValues.filter { item ->
                        selectedCountry.label(item) != null
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F, fill = false)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            FilterSizeCountrySelector(
                                selectedCountry = selectedCountry,
                                onCountryClick = { selectedCountry = it }
                            )

                            SharedLazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(bottom = 72.dp)
                            ) {
                                itemsIndexed(
                                    items = visibleSizeValues,
                                    key = { _, item -> item.id }
                                ) { index, item ->
                                    FilterSelectableRow(
                                        text = requireNotNull(selectedCountry.label(item)),
                                        selected = state.selectedIds.contains(item.id),
                                        onClick = { dispatch(FilterSizeIntent.ToggleFilterSizeValue(item.id)) }
                                    )

                                    if (index != visibleSizeValues.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(start = 48.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { dispatch(FilterSizeIntent.ConfirmFilterSizeValues) },
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
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FilterSizeSheetPreview(
    @PreviewParameter(FilterSizeSheetStateProvider::class) state: FilterSizeModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FilterSizeSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class FilterSizeSheetStateProvider: PreviewParameterProvider<FilterSizeModel> {
    override val values: Sequence<FilterSizeModel> = sequenceOf(
        FilterSizeModel(
            entity = FilterValuesEntity(
                chipId = "size",
                title = "Размер",
                items = listOf(
                    FilterValueItemEntity(
                        id = "size_10986",
                        label = "RU 36",
                        labelItalian = "IT 34 | RU 36",
                        labelFrench = "FR 30 | RU 36",
                        labelInternational = "XXXS | RU 36"
                    ),
                    FilterValueItemEntity(
                        id = "size_11711",
                        label = "RU 38",
                        labelItalian = "IT 36 | RU 38",
                        labelFrench = "FR 32 | RU 38",
                        labelInternational = "XXXS | RU 38"
                    ),
                    FilterValueItemEntity(
                        id = "size_10031",
                        label = "RU 40",
                        labelItalian = "IT 38 | RU 40",
                        labelFrench = "FR 34 | RU 40",
                        labelInternational = "XXS | RU 40"
                    ),
                    FilterValueItemEntity(
                        id = "size_11139",
                        label = "RU 42",
                        labelItalian = "IT 40 | RU 42",
                        labelFrench = "FR 36 | RU 42",
                        labelInternational = "XS | RU 42"
                    )
                )
            ),
            selectedIds = setOf("size_10986", "size_10031"),
            quantityEntity = FilterValuesQuantityEntity(
                chipId = "size",
                quantity = 128
            ),
            isProductsQuantityLoading = false,
            isLoading = false
        ),
        FilterSizeModel(
            entity = FilterValuesEntity(
                chipId = "size",
                title = "Размер"
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
