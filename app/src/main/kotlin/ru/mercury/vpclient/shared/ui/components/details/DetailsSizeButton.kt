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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.SizeState
import ru.mercury.vpclient.shared.ui.preview.SizeStateProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

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
            .border(1.dp, outerBorderColor, RoundedCornerShape(4.dp))
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
                letterSpacing = .2.sp
            )
        )

        Text(
            text = state.bottomText,
            style = MaterialTheme.typography.regular14.copy(
                color = if (state.enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun DetailsSizeButtonPreview(
    @PreviewParameter(SizeStateProvider::class) state: SizeState
) {
    DetailsSizeButton(
        state = state,
        onClick = {},
        modifier = Modifier.padding(8.dp)
    )
}
