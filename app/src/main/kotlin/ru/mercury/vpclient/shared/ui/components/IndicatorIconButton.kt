package ru.mercury.vpclient.shared.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

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
@FontScalePreviews
@Composable
private fun IndicatorIconButtonPreview(
    @PreviewParameter(BooleanParameterProvider::class) showIndicator: Boolean
) {
    IndicatorIconButton(
        icon = Chat24,
        showIndicator = showIndicator,
        onClick = {}
    )
}
