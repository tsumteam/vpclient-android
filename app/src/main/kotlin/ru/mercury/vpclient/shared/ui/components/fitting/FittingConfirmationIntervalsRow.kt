package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval

@Composable
fun FittingConfirmationIntervalsRow(
    intervals: List<FittingConfirmationDeliveryInterval>,
    selectedIntervalId: String?,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = intervals,
            key = { interval -> interval.id }
        ) { interval ->
            FittingConfirmationChip(
                text = interval.timeTitle,
                selected = selectedIntervalId == interval.id,
                height = 27.dp,
                onClick = { onIntervalClick(interval.id) }
            )
        }
    }
}
