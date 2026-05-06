package ru.mercury.vpclient.shared.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

@Composable
fun BasketIconButton(
    text: String,
    showIndicator: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        BasketIcon(
            text = text,
            showIndicator = showIndicator
        )
    }
}

@FontScalePreviews
@Composable
private fun BasketIconButtonPreview(
    @PreviewParameter(BooleanParameterProvider::class) showIndicator: Boolean
) {
    ClientTheme {
        BasketIconButton(
            text = "1",
            showIndicator = showIndicator,
            onClick = {}
        )
    }
}
