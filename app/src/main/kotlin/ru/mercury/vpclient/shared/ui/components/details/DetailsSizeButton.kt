package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

data class SizeState(
    val topText: String,
    val bottomText: String,
    val selected: Boolean,
    val enabled: Boolean
)

@Composable
fun DetailsSizeButton(
    state: SizeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val outerBorderColor by animateColorAsState(
        targetValue = if (state.selected) MaterialTheme.colorScheme.error else Color.Transparent,
        label = "size_box_outer_border"
    )

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = outerBorderColor,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(2.dp)
            .size(width = 50.dp, height = 58.dp)
            .border(
                width = 1.dp,
                color = if (state.enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick)
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.topText,
            style = MaterialTheme.typography.regular14.copy(
                color = if (state.enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )

        Text(
            text = state.bottomText,
            style = MaterialTheme.typography.regular14.copy(
                color = if (state.enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun DetailsSizeButtonPreview(
    @PreviewParameter(SizeStateProvider::class) state: SizeState
) {
    DetailsSizeButton(
        state = state,
        onClick = {}
    )
}

private class SizeStateProvider: PreviewParameterProvider<SizeState> {
    override val values: Sequence<SizeState> = sequenceOf(
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = false,
            enabled = true
        ),
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = true,
            enabled = true
        ),
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = false,
            enabled = false
        ),
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = true,
            enabled = false
        )
    )
}
