package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium13

@Composable
fun FittingConfirmationMultipleDeliveryContent(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FittingConfirmationInfoText(
            text = stringResource(ClientStrings.FittingConfirmationMultipleDeliveryInfo),
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp)
        )

        SharedTabRow(
            state = SharedTabRowState(
                selectedIndex = when (state.deliveryMode) {
                    FittingConfirmationDeliveryMode.Multiple -> 0
                    FittingConfirmationDeliveryMode.Single -> 1
                },
                firstTabText = stringResource(ClientStrings.FittingConfirmationMultipleDeliveries),
                secondTabText = stringResource(ClientStrings.FittingConfirmationSingleDelivery),
                onFirstTabClick = {
                    dispatch(
                        FittingConfirmationIntent.SelectDeliveryMode(FittingConfirmationDeliveryMode.Multiple)
                    )
                },
                onSecondTabClick = {
                    dispatch(
                        FittingConfirmationIntent.SelectDeliveryMode(FittingConfirmationDeliveryMode.Single)
                    )
                },
                isLoading = false
            ),
            textStyle = MaterialTheme.typography.medium13.copy(
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(start = 16.dp, top = 18.dp, end = 16.dp)
        )

        when (state.deliveryMode) {
            FittingConfirmationDeliveryMode.Multiple -> {
                state.deliveryGroups.forEach { group ->
                    FittingConfirmationDeliveryGroupCard(
                        group = group,
                        selectedDayId = state.selectedDeliveryDayIds[group.id],
                        selectedIntervalId = state.selectedDeliveryIntervalIds[group.id],
                        expanded = state.expandedDeliveryId == group.id,
                        onChangeTimeClick = {
                            dispatch(FittingConfirmationIntent.ChangeDeliveryTimeClick(group.id))
                        },
                        onDayClick = { dayId ->
                            dispatch(FittingConfirmationIntent.SelectDeliveryDay(group.id, dayId))
                        },
                        onIntervalClick = { intervalId ->
                            dispatch(FittingConfirmationIntent.SelectDeliveryInterval(group.id, intervalId))
                        }
                    )
                }
            }
            FittingConfirmationDeliveryMode.Single -> {
                FittingConfirmationSingleDeliveryContent(
                    intervals = state.singleIntervals,
                    selectedDayId = state.selectedSingleDayId,
                    selectedIntervalId = state.selectedSingleIntervalId,
                    onDayClick = { dayId -> dispatch(FittingConfirmationIntent.SelectSingleDay(dayId)) },
                    onIntervalClick = { intervalId ->
                        dispatch(FittingConfirmationIntent.SelectSingleInterval(intervalId))
                    }
                )
            }
        }
    }
}
