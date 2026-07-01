package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium12

data class FittingConfirmationTimeChipState(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit
)

@Composable
fun FittingConfirmationTimeChip(
    state: FittingConfirmationTimeChipState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(33.dp)
            .border(
                width = 2.dp,
                color = when {
                    state.selected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.background
                },
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = state.onClick)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(27.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(start = 12.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.text,
                maxLines = 1,
                style = MaterialTheme.typography.medium12.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationTimeChipPreview(
    @PreviewParameter(FittingConfirmationTimeChipStateProvider::class) state: FittingConfirmationTimeChipState
) {
    FittingConfirmationTimeChip(
        state = state
    )
}

private class FittingConfirmationTimeChipStateProvider: PreviewParameterProvider<FittingConfirmationTimeChipState> {
    override val values: Sequence<FittingConfirmationTimeChipState> = sequenceOf(
        FittingConfirmationTimeChipState(
            text = "10:00-12:00",
            selected = true,
            onClick = {}
        ),
        FittingConfirmationTimeChipState(
            text = "12:00-14:00",
            selected = false,
            onClick = {}
        )
    )
}
