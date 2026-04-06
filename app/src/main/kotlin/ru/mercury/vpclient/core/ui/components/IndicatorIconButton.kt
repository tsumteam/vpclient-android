package ru.mercury.vpclient.core.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

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

@FontScalePreviews
@Composable
private fun IndicatorIconButtonPreview(
    @PreviewParameter(BooleanParameterProvider::class) showIndicator: Boolean
) {
    ClientTheme {
        IndicatorIconButton(
            icon = Chat24,
            showIndicator = showIndicator,
            onClick = {}
        )
    }
}
