@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.filter_values_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.filter_values_sheet.intent.FilterValuesIntent
import ru.mercury.vpclient.features.filter_values_sheet.model.FilterValuesModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireQuantity
import ru.mercury.vpclient.shared.domain.mapper.values
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium16

@Composable
fun FilterValuesSheet(
    state: FilterValuesModel,
    dispatch: (FilterValuesIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(FilterValuesIntent.HideFilterValuesDialog) },
        sheetState = sheetState
    ) {
        FilterValuesSheetContent(
            state = state,
            dispatch = dispatch
        )
    }
}

@Composable
private fun FilterValuesSheetContent(
    state: FilterValuesModel,
    dispatch: (FilterValuesIntent) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
        ) {
            IconButton(
                onClick = { dispatch(FilterValuesIntent.HideFilterValuesDialog) },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Close24,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = state.entity.title,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 56.dp, vertical = 0.dp),
                style = MaterialTheme.typography.livretMedium19.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )

            SharedAnimatedVisibility(
                visible = state.selectedIds.isNotEmpty(),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                TextButton(
                    onClick = { dispatch(FilterValuesIntent.ResetFilterValues) },
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

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 4.dp)
                                .height(48.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F, fill = false)
                ) {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 72.dp)
                    ) {
                        itemsIndexed(
                            items = state.entity.values,
                            key = { _, item -> item.id }
                        ) { index, item ->
                            FilterSelectableRow(
                                text = item.label,
                                selected = state.selectedIds.contains(item.id),
                                onClick = { dispatch(FilterValuesIntent.ToggleFilterValue(item.id)) }
                            )

                            if (index != state.entity.values.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 48.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { dispatch(FilterValuesIntent.ConfirmFilterValues) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(52.dp)
                            .placeholder(
                                visible = state.isLoading,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ),
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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FilterValuesSheetPreview(
    @PreviewParameter(FilterValuesSheetStateProvider::class) state: FilterValuesModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        FilterValuesSheetContent(
            state = state,
            dispatch = {}
        )
    }
}

private class FilterValuesSheetStateProvider: PreviewParameterProvider<FilterValuesModel> {
    override val values: Sequence<FilterValuesModel> = sequenceOf(
        FilterValuesModel(
            entity = FilterValuesEntity(
                chipId = "attribute_length",
                title = "ДЛИНА",
                items = listOf(
                    FilterValueItemEntity(id = "attribute_length_mini", label = "Мини"),
                    FilterValueItemEntity(id = "attribute_length_midi", label = "Миди"),
                    FilterValueItemEntity(id = "attribute_length_maxi", label = "Макси"),
                    FilterValueItemEntity(id = "attribute_length_ankle", label = "До щиколотки")
                )
            ),
            selectedIds = setOf("attribute_length_midi", "attribute_length_maxi"),
            quantityEntity = FilterValuesQuantityEntity(
                chipId = "attribute_length",
                quantity = 128
            ),
            isProductsQuantityLoading = false,
            isLoading = false
        ),
        FilterValuesModel(
            entity = FilterValuesEntity(
                chipId = "attribute_length",
                title = "ДЛИНА"
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
