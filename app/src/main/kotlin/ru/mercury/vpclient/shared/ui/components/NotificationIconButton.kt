package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.icons.Notification24
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun NotificationIconButton(
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(42.dp)
    ) {
        IndicatorIcon(
            icon = Notification24,
            showIndicator = showBadge
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun NotificationIconButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showBadge: Boolean
) {
    NotificationIconButton(
        showBadge = showBadge,
        onClick = {}
    )
}
