@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_sort

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.entity.SortType
import ru.mercury.vpclient.shared.ktx.isResetButtonVisible
import ru.mercury.vpclient.shared.ui.components.filters.FilterListRow
import ru.mercury.vpclient.shared.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientDragHandle
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.SortTypeProvider
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium16
import ru.mercury.vpclient.shared.ui.theme.secondary5
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_sort.intent.SortIntent

@Composable
fun SortSheet(
    selectedSortType: SortType,
    dispatch: (SortIntent) -> Unit
) {
    var selectedSortType by remember(selectedSortType) { mutableStateOf(selectedSortType) }

    SortSheetContent(
        selectedSortType = selectedSortType,
        onSelectSortType = { sortType -> selectedSortType = sortType },
        dispatch = dispatch
    )
}

@Composable
private fun SortSheetContent(
    selectedSortType: SortType,
    onSelectSortType: (SortType) -> Unit,
    dispatch: (SortIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { dispatch(SortIntent.HideSortDialog) },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = Color.White,
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
                            dispatch(SortIntent.HideSortDialog)
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
                    text = stringResource(ClientStrings.SortTitle),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp, vertical = 0.dp),
                    style = MaterialTheme.typography.livretMedium19.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                ClientAnimatedVisibility(
                    visible = selectedSortType.isResetButtonVisible,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(
                        onClick = { onSelectSortType(SortType.OurChoice) },
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

            FilterListRow(
                text = stringResource(ClientStrings.SortOurChoice),
                selected = selectedSortType == SortType.OurChoice,
                onClick = { onSelectSortType(SortType.OurChoice) }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 48.dp),
                color = MaterialTheme.colorScheme.secondary5
            )

            FilterListRow(
                text = stringResource(ClientStrings.SortPriceAscending),
                selected = selectedSortType == SortType.PriceAscending,
                onClick = { onSelectSortType(SortType.PriceAscending) }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 48.dp),
                color = MaterialTheme.colorScheme.secondary5
            )

            FilterListRow(
                text = stringResource(ClientStrings.SortPriceDescending),
                selected = selectedSortType == SortType.PriceDescending,
                onClick = { onSelectSortType(SortType.PriceDescending) }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 48.dp),
                color = MaterialTheme.colorScheme.secondary5
            )

            FilterListRow(
                text = stringResource(ClientStrings.SortArrivalDateDescending),
                selected = selectedSortType == SortType.ArrivalDateDescending,
                onClick = { onSelectSortType(SortType.ArrivalDateDescending) }
            )

            ClientButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        dispatch(SortIntent.ConfirmSort(selectedSortType))
                    }
                },
                text = stringResource(ClientStrings.SortSelect),
                modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SortSheetPreview(
    @PreviewParameter(SortTypeProvider::class) selectedSortType: SortType
) {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            SortSheetContent(
                selectedSortType = selectedSortType,
                onSelectSortType = {},
                dispatch = {}
            )
        }
    }
}
