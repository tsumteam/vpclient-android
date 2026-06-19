package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun CartIconButton(
    text: String,
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(42.dp)
    ) {
        CartIcon(
            text = text,
            showBadge = showBadge
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartIconButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showBadge: Boolean
) {
    CartIconButton(
        text = "1",
        showBadge = showBadge,
        onClick = {}
    )
}
