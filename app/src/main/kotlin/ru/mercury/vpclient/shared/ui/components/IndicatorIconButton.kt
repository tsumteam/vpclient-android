package ru.mercury.vpclient.shared.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun IndicatorIconButton(
    icon: ImageVector,
    showIndicator: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        IndicatorIcon(
            icon = icon,
            showIndicator = showIndicator
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun IndicatorIconButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showIndicator: Boolean
) {
    IndicatorIconButton(
        icon = Chat24,
        showIndicator = showIndicator,
        onClick = {}
    )
}
