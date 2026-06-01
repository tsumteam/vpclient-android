package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval

@Composable
fun FittingConfirmationDaysRow(
    intervals: List<FittingConfirmationDeliveryInterval>,
    selectedDayId: String?,
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    val days = intervals.distinctBy { interval -> interval.dayId }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(days) { index, interval ->
            val dayId = selectedDayId ?: days.firstOrNull()?.dayId

            FittingConfirmationChip(
                text = interval.dayTitle,
                selected = dayId == interval.dayId || (dayId == null && index == 0),
                height = 52.dp,
                onClick = { onDayClick(interval.dayId) }
            )
        }
    }
}
