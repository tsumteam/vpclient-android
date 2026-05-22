package ru.mercury.vpclient.shared.ui.components.consultants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.components.cart.CartIcon
import ru.mercury.vpclient.shared.ui.preview.ConsultantCartActionButtonStateProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular11

@Composable
fun ConsultantCartActionButton(
    state: ConsultantCartActionButtonState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .defaultMinSize(minWidth = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable(onClick = state.onClick)
            .padding(start = 4.dp, top = 5.dp, end = 4.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top)
    ) {
        CartIcon(
            text = state.cartText,
            showBadge = state.showBadge
        )

        Text(
            text = state.label,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            style = MaterialTheme.typography.regular11.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun ConsultantCartActionButtonPreview(
    @PreviewParameter(ConsultantCartActionButtonStateProvider::class) state: ConsultantCartActionButtonState
) {
    ConsultantCartActionButton(
        state = state,
        modifier = Modifier
            .padding(16.dp)
            .width(69.dp)
    )
}
