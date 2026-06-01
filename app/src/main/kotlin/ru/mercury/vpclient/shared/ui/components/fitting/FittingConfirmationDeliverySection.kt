package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel

@Composable
fun FittingConfirmationDeliverySection(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        when {
            state.isIntervalsLoading -> {
                FittingConfirmationIntervalsLoading()
            }
            state.intervalsError != null -> {
                FittingConfirmationInfoText(
                    text = state.intervalsError
                )
            }
            state.isMultipleDeliveryAvailable -> {
                FittingConfirmationMultipleDeliveryContent(
                    state = state,
                    dispatch = dispatch
                )
            }
            else -> {
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
