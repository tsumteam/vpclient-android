package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.icons.Heart24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium11

data class BrandFavoriteIconButtonState(
    val text: String,
    val visible: Boolean,
    val onClick: () -> Unit
)

@Composable
fun BrandFavoriteIconButton(
    state: BrandFavoriteIconButtonState,
    modifier: Modifier = Modifier
) {
    SharedAnimatedVisibility(
        visible = state.visible,
        modifier = modifier
    ) {
        IconButton(
            onClick = state.onClick,
            modifier = Modifier.size(42.dp)
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Heart24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = state.text,
                    modifier = Modifier.padding(end = .5.dp, bottom = 1.dp),
                    maxLines = 1,
                    style = MaterialTheme.typography.medium11.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandFavoriteIconButtonPreview(
    @PreviewParameter(BrandFavoriteIconButtonStateProvider::class) state: BrandFavoriteIconButtonState
) {
    BrandFavoriteIconButton(
        state = state
    )
}

private class BrandFavoriteIconButtonStateProvider: PreviewParameterProvider<BrandFavoriteIconButtonState> {
    override val values: Sequence<BrandFavoriteIconButtonState> = sequenceOf(
        BrandFavoriteIconButtonState(
            text = "2",
            visible = true,
            onClick = {}
        ),
        BrandFavoriteIconButtonState(
            text = "22",
            visible = true,
            onClick = {}
        )
    )
}
