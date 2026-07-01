package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

data class FittingConfirmationSingleDeliveryContentState(
    val intervals: List<FittingConfirmationDeliveryInterval>,
    val selectedDayId: String?,
    val selectedIntervalId: String?,
    val onDayClick: (String) -> Unit,
    val onIntervalClick: (String) -> Unit
)

@Composable
fun FittingConfirmationSingleDeliveryContent(
    state: FittingConfirmationSingleDeliveryContentState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        when {
            state.intervals.isEmpty() -> {
                Text(
                    text = stringResource(ClientStrings.FittingConfirmationIntervalsEmpty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.regular14.copy(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Start
                    )
                )
            }
            else -> {
                FittingConfirmationDaysRow(
                    state = FittingConfirmationDaysRowState(
                        intervals = state.intervals,
                        selectedDayId = state.selectedDayId,
                        onDayClick = state.onDayClick
                    )
                )

                FittingConfirmationIntervalsRow(
                    state = FittingConfirmationIntervalsRowState(
                        intervals = state.intervals.filter { interval ->
                            interval.dayId == (state.selectedDayId ?: state.intervals.firstOrNull()?.dayId)
                        },
                        selectedIntervalId = state.selectedIntervalId,
                        onIntervalClick = state.onIntervalClick
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationSingleDeliveryContentPreview(
    @PreviewParameter(FittingConfirmationSingleDeliveryContentStateProvider::class) state: FittingConfirmationSingleDeliveryContentState
) {
    FittingConfirmationSingleDeliveryContent(
        state = state
    )
}

private class FittingConfirmationSingleDeliveryContentStateProvider:
    PreviewParameterProvider<FittingConfirmationSingleDeliveryContentState> {
    private val intervals = listOf(
        FittingConfirmationDeliveryInterval(
            id = "2026-05-13T10:00_2026-05-13T12:00",
            dayId = "2026-05-13",
            dayTitle = "13 мая",
            timeTitle = "10:00-12:00",
            summary = "13 мая с 10:00 до 12:00"
        ),
        FittingConfirmationDeliveryInterval(
            id = "2026-05-13T12:00_2026-05-13T14:00",
            dayId = "2026-05-13",
            dayTitle = "13 мая",
            timeTitle = "12:00-14:00",
            summary = "13 мая с 12:00 до 14:00"
        ),
        FittingConfirmationDeliveryInterval(
            id = "2026-06-13T10:00_2026-06-13T12:00",
            dayId = "2026-06-13",
            dayTitle = "13 июня",
            timeTitle = "10:00-12:00",
            summary = "13 июня с 10:00 до 12:00"
        )
    )

    override val values: Sequence<FittingConfirmationSingleDeliveryContentState> = sequenceOf(
        FittingConfirmationSingleDeliveryContentState(
            intervals = emptyList(),
            selectedDayId = null,
            selectedIntervalId = null,
            onDayClick = {},
            onIntervalClick = {}
        ),
        FittingConfirmationSingleDeliveryContentState(
            intervals = intervals,
            selectedDayId = intervals.first().dayId,
            selectedIntervalId = intervals.first().id,
            onDayClick = {},
            onIntervalClick = {}
        )
    )
}
