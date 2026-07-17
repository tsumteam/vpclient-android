package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class FittingConfirmationIntervalsRowState(
    val intervals: List<FittingConfirmationDeliveryInterval>,
    val selectedIntervalId: String?,
    val onIntervalClick: (String) -> Unit
)

@Composable
fun FittingConfirmationIntervalsRow(
    state: FittingConfirmationIntervalsRowState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp),
    paddingTop: Dp = 12.dp
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = paddingTop),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.intervals,
            key = { interval -> interval.id }
        ) { interval ->
            FittingConfirmationTimeChip(
                state = FittingConfirmationTimeChipState(
                    text = interval.timeTitle,
                    selected = state.selectedIntervalId == interval.id,
                    onClick = { state.onIntervalClick(interval.id) }
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationIntervalsRowPreview(
    @PreviewParameter(FittingConfirmationIntervalsRowStateProvider::class) state: FittingConfirmationIntervalsRowState
) {
    FittingConfirmationIntervalsRow(
        state = state
    )
}

private class FittingConfirmationIntervalsRowStateProvider: PreviewParameterProvider<FittingConfirmationIntervalsRowState> {
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
            id = "2026-05-13T14:00_2026-05-13T16:00",
            dayId = "2026-05-13",
            dayTitle = "13 мая",
            timeTitle = "14:00-16:00",
            summary = "13 мая с 14:00 до 16:00"
        )
    )

    override val values: Sequence<FittingConfirmationIntervalsRowState> = sequenceOf(
        FittingConfirmationIntervalsRowState(
            intervals = intervals,
            selectedIntervalId = intervals.first().id,
            onIntervalClick = {}
        ),
        FittingConfirmationIntervalsRowState(
            intervals = intervals,
            selectedIntervalId = intervals[1].id,
            onIntervalClick = {}
        )
    )
}
