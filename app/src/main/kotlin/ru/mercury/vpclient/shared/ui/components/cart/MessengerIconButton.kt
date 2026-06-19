package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun MessengerIconButton(
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(42.dp)
    ) {
        Box(
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Chat24,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onBackground
            )

            AnimatedContent(
                targetState = showBadge,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "messenger_badge_animation",
                modifier = Modifier.align(Alignment.TopEnd)
            ) { visible ->
                if (visible) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp, end = 2.dp)
                            .size(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.error,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerIconButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showBadge: Boolean
) {
    MessengerIconButton(
        showBadge = showBadge,
        onClick = {}
    )
}
