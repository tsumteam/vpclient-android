package ru.mercury.vpclient.shared.ui.components.notification

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

data class NotificationFilterChipState(
    val text: String,
    val isSelected: Boolean,
    val isEnabled: Boolean,
    val onClick: () -> Unit
)

@Composable
fun NotificationFilterChip(
    state: NotificationFilterChipState,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            state.isSelected -> MaterialTheme.colorScheme.onBackground
            else -> MaterialTheme.colorScheme.background
        },
        label = "notification_filter_background"
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            state.isSelected -> MaterialTheme.colorScheme.background
            else -> MaterialTheme.colorScheme.onBackground
        },
        label = "notification_filter_content"
    )

    Text(
        text = state.text,
        modifier = modifier
            .height(34.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(enabled = state.isEnabled, onClick = state.onClick)
            .padding(horizontal = 8.dp)
            .wrapContentSize(Alignment.Center),
        style = MaterialTheme.typography.regular14.copy(
            color = contentColor,
            lineHeight = 20.sp,
            letterSpacing = .2.sp,
            textAlign = TextAlign.Center
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun NotificationFilterChipPreview(
    @PreviewParameter(NotificationFilterChipStateProvider::class) state: NotificationFilterChipState
) {
    NotificationFilterChip(
        state = state
    )
}

private class NotificationFilterChipStateProvider: PreviewParameterProvider<NotificationFilterChipState> {
    override val values: Sequence<NotificationFilterChipState> = sequenceOf(
        NotificationFilterChipState(
            text = "ВСЕ",
            isSelected = true,
            isEnabled = true,
            onClick = {}
        ),
        NotificationFilterChipState(
            text = "ОТ КОНСУЛЬТАНТОВ",
            isSelected = false,
            isEnabled = true,
            onClick = {}
        )
    )
}
