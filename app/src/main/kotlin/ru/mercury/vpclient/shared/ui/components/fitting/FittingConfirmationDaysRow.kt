package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class FittingConfirmationDaysRowState(
    val intervals: List<FittingConfirmationDeliveryInterval>,
    val selectedDayId: String?,
    val onDayClick: (String) -> Unit
)

@Composable
fun FittingConfirmationDaysRow(
    state: FittingConfirmationDaysRowState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    val days = state.intervals.distinctBy { interval -> interval.dayId }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = days
        ) { index, interval ->
            val dayId = state.selectedDayId ?: days.firstOrNull()?.dayId

            FittingConfirmationDateChip(
                state = FittingConfirmationDateChipState(
                    dayId = interval.dayId,
                    dayTitle = interval.dayTitle,
                    selected = dayId == interval.dayId || (dayId == null && index == 0),
                    onClick = { state.onDayClick(interval.dayId) }
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationDaysRowPreview(
    @PreviewParameter(FittingConfirmationDaysRowStateProvider::class) state: FittingConfirmationDaysRowState
) {
    FittingConfirmationDaysRow(
        state = state
    )
}

private class FittingConfirmationDaysRowStateProvider: PreviewParameterProvider<FittingConfirmationDaysRowState> {
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

    override val values: Sequence<FittingConfirmationDaysRowState> = sequenceOf(
        FittingConfirmationDaysRowState(
            intervals = intervals,
            selectedDayId = intervals.first().dayId,
            onDayClick = {}
        ),
        FittingConfirmationDaysRowState(
            intervals = intervals,
            selectedDayId = intervals.last().dayId,
            onDayClick = {}
        )
    )
}
