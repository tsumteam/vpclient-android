@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_confirmation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

private val FITTING_CONFIRMATION_DAYS = listOf("Завтра", "Сб", "Вс", "Пн", "Вт")
private val FITTING_CONFIRMATION_INTERVALS = listOf("10:00-12:00", "12:00-14:00", "14:00-16:00", "16:00-18:00")

@Composable
fun FittingConfirmationScreen(
    route: FittingConfirmationRoute,
    viewModel: FittingConfirmationViewModel = hiltViewModel<FittingConfirmationViewModel, FittingConfirmationViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    FittingConfirmationScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun FittingConfirmationScreenContent(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit
) {
    val places = listOf(
        stringResource(ClientStrings.FittingConfirmationPlaceBoutique),
        stringResource(ClientStrings.FittingConfirmationPlaceHome),
        stringResource(ClientStrings.FittingConfirmationPlaceOther)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FittingConfirmationIntent.BackClick) },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ClientButton(
                onClick = { dispatch(FittingConfirmationIntent.ConfirmClick) },
                text = stringResource(ClientStrings.FittingConfirmationConfirm),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                enabled = state.isConfirmEnabled
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = 96.dp
            )
        ) {
            item {
                FittingConfirmationSectionTitle(
                    text = stringResource(ClientStrings.FittingConfirmationPlaceTitle)
                )
            }

            itemsIndexed(
                items = places
            ) { index, place ->
                FittingConfirmationPlaceRow(
                    text = place,
                    selected = state.selectedPlaceIndex == index,
                    onClick = { dispatch(FittingConfirmationIntent.SelectPlace(index)) }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            item {
                FittingConfirmationDaysRow(
                    selectedDayIndex = state.selectedDayIndex,
                    onDayClick = { index -> dispatch(FittingConfirmationIntent.SelectDay(index)) }
                )
            }

            item {
                FittingConfirmationIntervalsRow(
                    selectedInterval = state.selectedInterval,
                    onIntervalClick = { interval -> dispatch(FittingConfirmationIntent.SelectInterval(interval)) }
                )
            }
        }
    }
}

@Composable
private fun FittingConfirmationSectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular14.copy(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun FittingConfirmationPlaceRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@Composable
private fun FittingConfirmationDaysRow(
    selectedDayIndex: Int,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(FITTING_CONFIRMATION_DAYS) { index, day ->
            FittingConfirmationChip(
                text = day,
                selected = selectedDayIndex == index,
                onClick = { onDayClick(index) }
            )
        }
    }
}

@Composable
private fun FittingConfirmationIntervalsRow(
    selectedInterval: String?,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(FITTING_CONFIRMATION_INTERVALS) { _, interval ->
            FittingConfirmationChip(
                text = interval,
                selected = selectedInterval == interval,
                onClick = { onIntervalClick(interval) }
            )
        }
    }
}

@Composable
private fun FittingConfirmationChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(40.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = when {
            selected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.background
        },
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Box(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                maxLines = 1,
                style = MaterialTheme.typography.medium15.copy(
                    color = when {
                        selected -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onBackground
                    },
                    letterSpacing = .2.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingConfirmationScreenContentPreview() {
    FittingConfirmationScreenContent(
        state = FittingConfirmationModel(
            route = FittingConfirmationRoute(productIds = listOf("1", "2")),
            products = CartProductProvider().values.toList()
        ),
        dispatch = {}
    )
}
