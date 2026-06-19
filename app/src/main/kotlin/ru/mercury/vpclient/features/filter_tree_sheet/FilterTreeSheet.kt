@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.filter_tree_sheet

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.filter_tree_sheet.intent.FilterTreeIntent
import ru.mercury.vpclient.features.filter_tree_sheet.model.FilterTreeModel
import ru.mercury.vpclient.shared.data.entity.FilterTreeValue
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireQuantity
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.icons.Check24
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium16
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FilterTreeSheet(
    state: FilterTreeModel,
    dispatch: (FilterTreeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(FilterTreeIntent.HideFilterTreeDialog) },
        sheetState = sheetState
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.title.uppercase(),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FilterTreeIntent.HideFilterTreeDialog) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    SharedAnimatedVisibility(
                        visible = state.selectedIds.isNotEmpty()
                    ) {
                        TextButton(
                            onClick = { dispatch(FilterTreeIntent.ResetFilterValues) },
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

            SharedAnimatedVisibility(
                visible = state.currentParentId != null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { dispatch(FilterTreeIntent.NavigateBackInFilterTree) }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ChevronStart24,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = state.currentParentLabel.orEmpty(),
                        modifier = Modifier.padding(start = 12.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
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
                        items = state.values,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        when {
                            item.hasChildren -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clickable { dispatch(FilterTreeIntent.NavigateInFilterTree(item.id)) }
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Check24,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Transparent
                                    )

                                    Text(
                                        text = item.label,
                                        modifier = Modifier
                                            .weight(1F)
                                            .padding(start = 24.dp, end = 12.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.regular15.copy(
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    )

                                    Icon(
                                        imageVector = ChevronEnd24,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            else -> {
                                FilterSelectableRow(
                                    text = item.label,
                                    selected = item.id in state.selectedIds,
                                    onClick = { dispatch(FilterTreeIntent.ToggleFilterValue(item.id)) }
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

                Button(
                    onClick = { dispatch(FilterTreeIntent.ConfirmFilterValues) },
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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FilterTreeSheetPreview(
    @PreviewParameter(FilterTreeModelProvider::class) state: FilterTreeModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FilterTreeSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class FilterTreeModelProvider: PreviewParameterProvider<FilterTreeModel> {
    override val values: Sequence<FilterTreeModel> = sequenceOf(
        FilterTreeModel(
            title = "Category",
            currentParentId = null,
            currentParentLabel = null,
            values = listOf(
                FilterTreeValue(id = "1", label = "Outerwear", hasChildren = true),
                FilterTreeValue(id = "2", label = "Dresses", hasChildren = false)
            ),
            selectedIds = setOf("2"),
            quantityEntity = FilterValuesQuantityEntity("category", 42),
            isProductsQuantityLoading = false
        ),
        FilterTreeModel(
            title = "Category",
            currentParentId = "1",
            currentParentLabel = "Outerwear",
            values = listOf(
                FilterTreeValue(id = "3", label = "Coats", hasChildren = false),
                FilterTreeValue(id = "4", label = "Jackets", hasChildren = false)
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity("category", 0),
            isProductsQuantityLoading = true
        )
    )
}
