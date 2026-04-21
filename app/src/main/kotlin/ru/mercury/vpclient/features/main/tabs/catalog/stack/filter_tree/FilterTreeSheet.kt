@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_tree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.intent.FilterIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_tree.model.FilterTreeSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_tree.model.FilterTreeValue
import ru.mercury.vpclient.shared.domain.mapper.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireQuantity
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientDragHandle
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.icons.Check24
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium16
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FilterTreeSheet(
    state: FilterTreeSheetState,
    dispatch: (FilterIntent) -> Unit
) {
    FilterTreeSheetContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun FilterTreeSheetContent(
    state: FilterTreeSheetState,
    dispatch: (FilterIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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
                    visible = state.selectedIds.isNotEmpty(),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(
                        onClick = { dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet())) },
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

            ClientAnimatedVisibility(
                visible = state.currentParentId != null
            ) {
                TreeBackRow(
                    label = state.currentParentLabel.orEmpty(),
                    onClick = { dispatch(FilterIntent.NavigateBackInFilterTree) }
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
                        items = state.values,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        when {
                            item.hasChildren -> {
                                FilterTreeParentRow(
                                    value = item,
                                    onClick = { dispatch(FilterIntent.NavigateInFilterTree(item.id)) }
                                )
                            }
                            else -> {
                                FilterSelectableRow(
                                    text = item.label,
                                    selected = item.id in state.selectedIds,
                                    onClick = { dispatch(FilterIntent.ToggleFilterDialogValue(item.id)) }
                                )
                            }
                        }

                        if (index != state.values.lastIndex) {
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
                            dispatch(FilterIntent.ConfirmFilterValues)
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

@Composable
private fun TreeBackRow(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ChevronStart24,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
private fun FilterTreeParentRow(
    value: FilterTreeValue,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Check24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Transparent
        )

        Text(
            text = value.label,
            modifier = Modifier
                .weight(1F)
                .padding(start = 24.dp, top = 0.dp, end = 12.dp, bottom = 0.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Icon(
            imageVector = ChevronStart24,
            contentDescription = null,
            modifier = Modifier.rotate(180F),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun FilterTreeSheetPreview() {
    ClientTheme {
        Box(
            modifier = Modifier.background(Color.Gray)
        ) {
            FilterTreeSheet(
                state = FilterTreeSheetState(
                    title = "Category",
                    currentParentId = null,
                    currentParentLabel = null,
                    values = listOf(
                        FilterTreeValue(id = "1", label = "Outerwear", hasChildren = true),
                        FilterTreeValue(id = "2", label = "Dresses", hasChildren = false)
                    ),
                    selectedIds = setOf("2"),
                    quantityEntity = ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity("category", 42),
                    isProductsQuantityLoading = false
                ),
                dispatch = {}
            )
        }
    }
}
