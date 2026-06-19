@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.fitting_info.intent.FittingInfoIntent
import ru.mercury.vpclient.features.fitting_info.model.FittingInfoModel
import ru.mercury.vpclient.features.fitting_info.navigation.FittingInfoRoute
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.InfoItem
import ru.mercury.vpclient.shared.ui.components.InfoItemState
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun FittingInfoScreen(
    route: FittingInfoRoute,
    viewModel: FittingInfoViewModel = hiltViewModel<FittingInfoViewModel, FittingInfoViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    FittingInfoScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun FittingInfoScreenContent(
    state: FittingInfoModel,
    dispatch: (FittingInfoIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.FittingInfoTitle),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FittingInfoIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
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
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoAddressLabel),
                        value = state.address
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoDeliveryTimeLabel),
                        value = state.deliveryDate
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoAssemblyMethodLabel),
                        value = stringResource(ClientStrings.FittingInfoLogisticsService)
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoDeliveryMethodLabel),
                        value = stringResource(ClientStrings.FittingInfoLogisticsService)
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoSalesRepresentativeLabel),
                        value = stringResource(ClientStrings.FittingInfoSalesRepresentative)
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoPhoneLabel),
                        value = stringResource(ClientStrings.FittingInfoPhone)
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.FittingInfoVehicleNumberLabel),
                        value = stringResource(ClientStrings.FittingInfoVehicleNumber)
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FittingInfoScreenContentPreview(
    @PreviewParameter(FittingInfoModelProvider::class) state: FittingInfoModel
) {
    FittingInfoScreenContent(
        state = state,
        dispatch = {}
    )
}

private class FittingInfoModelProvider: PreviewParameterProvider<FittingInfoModel> {
    override val values: Sequence<FittingInfoModel> = sequenceOf(
        FittingInfoModel(
            address = "Brioni, Третьяковский проезд",
            deliveryDate = "13 мая 2026 с 10:00 до 12:00"
        )
    )
}
