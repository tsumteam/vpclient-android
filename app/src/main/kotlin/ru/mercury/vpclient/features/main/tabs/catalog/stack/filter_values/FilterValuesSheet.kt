@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.ktx.quantityWithThousandsSeparator
import ru.mercury.vpclient.core.ktx.requireQuantity
import ru.mercury.vpclient.core.ktx.values
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.core.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.core.ui.components.system.ClientButton
import ru.mercury.vpclient.core.ui.components.system.ClientDragHandle
import ru.mercury.vpclient.core.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.core.ui.icons.Close24
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.FilterValuesSheetStateProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.shimmer
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium19
import ru.mercury.vpclient.core.ui.theme.medium16
import ru.mercury.vpclient.core.ui.theme.secondary5
import ru.mercury.vpclient.core.ui.theme.surface4
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.intent.FilterValuesIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.model.FilterValuesSheetState

@Composable
fun FilterValuesSheet(
    state: FilterValuesSheetState,
    dispatch: (FilterValuesIntent) -> Unit
) {
    FilterValuesSheetContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun FilterValuesSheetContent(
    state: FilterValuesSheetState,
    dispatch: (FilterValuesIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { dispatch(FilterValuesIntent.HideFilterValuesDialog) },
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
                            dispatch(FilterValuesIntent.HideFilterValuesDialog)
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
                    text = state.entity.title,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp, vertical = 0.dp),
                    style = MaterialTheme.typography.livretMedium19.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )

                ClientAnimatedVisibility(
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
                                        color = MaterialTheme.colorScheme.surface4,
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
                                color = MaterialTheme.colorScheme.surface4,
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
                        ClientLazyColumn(
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
                                        color = MaterialTheme.colorScheme.secondary5
                                    )
                                }
                            }
                        }

                        ClientButton(
                            onClick = {
                                scope.launch {
                                    sheetState.hide()
                                    dispatch(FilterValuesIntent.ConfirmFilterValues)
                                }
                            },
                            text = pluralStringResource(ClientStrings.FilterShowProductsQuantity, state.quantityEntity.requireQuantity, state.quantityEntity.quantityWithThousandsSeparator),
                            loading = state.isProductsQuantityLoading,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 16.dp, vertical = 0.dp)
                                .placeholder(
                                    visible = state.isLoading,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surface4,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun FilterValuesSheetPreview(
    @PreviewParameter(FilterValuesSheetStateProvider::class) state: FilterValuesSheetState
) {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            FilterValuesSheet(
                state = state,
                dispatch = {}
            )
        }
    }
}
