package ru.mercury.vpclient.core.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun IndicatorIcon(
    icon: ImageVector?,
    showIndicator: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(24.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        AnimatedContent(
            targetState = showIndicator,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "indicator_animation",
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

@FontScalePreviews
@Composable
private fun IndicatorIconPreview(
    @PreviewParameter(BooleanParameterProvider::class) showIndicator: Boolean
) {
    ClientTheme {
        IndicatorIcon(
            icon = Chat24,
            showIndicator = showIndicator
        )
    }
}
