@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.filter_sort_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.filter_sort_sheet.intent.SortIntent
import ru.mercury.vpclient.features.filter_sort_sheet.model.SortModel
import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.domain.mapper.isResetButtonVisible
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.filters.FilterListRow
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium16

@Composable
fun FilterSortSheet(
    state: SortModel,
    dispatch: (SortIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(SortIntent.HideSortDialog) },
        sheetState = sheetState
    ) {
        var selectedSortType by remember(state.selectedSortType) { mutableStateOf(state.selectedSortType) }

        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.SortTitle),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(SortIntent.HideSortDialog) }
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
                        visible = selectedSortType.isResetButtonVisible
                    ) {
                        TextButton(
                            onClick = { selectedSortType = SortType.OurChoice },
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

            FilterListRow(
                text = stringResource(ClientStrings.SortOurChoice),
                selected = selectedSortType == SortType.OurChoice,
                onClick = { selectedSortType = SortType.OurChoice }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 48.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            FilterListRow(
                text = stringResource(ClientStrings.SortPriceAscending),
                selected = selectedSortType == SortType.PriceAscending,
                onClick = { selectedSortType = SortType.PriceAscending }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 48.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            FilterListRow(
                text = stringResource(ClientStrings.SortPriceDescending),
                selected = selectedSortType == SortType.PriceDescending,
                onClick = { selectedSortType = SortType.PriceDescending }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 48.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            FilterListRow(
                text = stringResource(ClientStrings.SortArrivalDateDescending),
                selected = selectedSortType == SortType.ArrivalDateDescending,
                onClick = { selectedSortType = SortType.ArrivalDateDescending }
            )

            Button(
                onClick = { dispatch(SortIntent.ConfirmSort(selectedSortType)) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.SortSelect),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FilterSortSheetPreview(
    @PreviewParameter(SortTypeProvider::class) selectedSortType: SortType
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FilterSortSheet(
            state = SortModel(
                selectedSortType = selectedSortType
            ),
            dispatch = {}
        )
    }
}

private class SortTypeProvider: PreviewParameterProvider<SortType> {
    override val values: Sequence<SortType> = sequenceOf(
        SortType.OurChoice,
        SortType.PriceAscending,
        SortType.PriceDescending,
        SortType.ArrivalDateDescending
    )
}
