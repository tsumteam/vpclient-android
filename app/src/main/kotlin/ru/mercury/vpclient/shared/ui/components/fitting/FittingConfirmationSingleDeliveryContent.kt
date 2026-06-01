package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun FittingConfirmationSingleDeliveryContent(
    intervals: List<FittingConfirmationDeliveryInterval>,
    selectedDayId: String?,
    selectedIntervalId: String?,
    onDayClick: (String) -> Unit,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        when {
            intervals.isEmpty() -> {
                FittingConfirmationInfoText(
                    text = stringResource(ClientStrings.FittingConfirmationIntervalsEmpty)
                )
            }
            else -> {
                FittingConfirmationDaysRow(
                    intervals = intervals,
                    selectedDayId = selectedDayId,
                    onDayClick = onDayClick
                )

                FittingConfirmationIntervalsRow(
                    intervals = intervals.filter { interval ->
                        interval.dayId == (selectedDayId ?: intervals.firstOrNull()?.dayId)
                    },
                    selectedIntervalId = selectedIntervalId,
                    onIntervalClick = onIntervalClick
                )
            }
        }
    }
}
