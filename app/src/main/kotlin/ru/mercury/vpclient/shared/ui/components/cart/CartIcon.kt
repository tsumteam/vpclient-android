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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.icons.Basket24
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular10

@Composable
fun CartIcon(
    text: String,
    showBadge: Boolean
) {
    Box(
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            imageVector = Basket24,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(top = 6.dp)
                .align(Alignment.Center),
            maxLines = 1,
            style = MaterialTheme.typography.regular10.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )

        AnimatedContent(
            targetState = showBadge,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "cart_badge_animation",
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

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun CartIconPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showBadge: Boolean
) {
    CartIcon(
        text = "1",
        showBadge = showBadge
    )
}
