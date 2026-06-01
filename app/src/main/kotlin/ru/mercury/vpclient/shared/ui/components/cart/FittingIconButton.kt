package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun FittingIconButton(
    text: String,
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(42.dp)
    ) {
        FittingIcon(
            text = text,
            showBadge = showBadge
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingIconButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showBadge: Boolean
) {
    FittingIconButton(
        text = "1",
        showBadge = showBadge,
        onClick = {}
    )
}
