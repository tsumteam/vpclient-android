package ru.mercury.vpclient.shared.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

@Composable
fun CartIconButton(
    text: String,
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        CartIcon(
            text = text,
            showBadge = showBadge
        )
    }
}

@FontScalePreviews
@Composable
private fun CartIconButtonPreview(
    @PreviewParameter(BooleanParameterProvider::class) showBadge: Boolean
) {
    ClientTheme {
        CartIconButton(
            text = "1",
            showBadge = showBadge,
            onClick = {}
        )
    }
}
