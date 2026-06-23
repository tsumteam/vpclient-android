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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.components.cart.FittingIcon
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular11

data class ConsultantFittingActionButtonState(
    val label: String,
    val fittingText: String,
    val showBadge: Boolean,
    val onClick: () -> Unit
)

@Composable
fun ConsultantFittingActionButton(
    state: ConsultantFittingActionButtonState,
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
        FittingIcon(
            text = state.fittingText,
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
@Preview(showBackground = true)
@Composable
private fun ConsultantFittingActionButtonPreview(
    @PreviewParameter(ConsultantFittingActionButtonStateProvider::class) state: ConsultantFittingActionButtonState
) {
    ConsultantFittingActionButton(
        state = state,
        modifier = Modifier.width(69.dp)
    )
}

private class ConsultantFittingActionButtonStateProvider: PreviewParameterProvider<ConsultantFittingActionButtonState> {
    override val values: Sequence<ConsultantFittingActionButtonState> = sequenceOf(
        ConsultantFittingActionButtonState(
            label = "Примерка",
            fittingText = "",
            showBadge = false,
            onClick = {}
        ),
        ConsultantFittingActionButtonState(
            label = "Примерка",
            fittingText = "2",
            showBadge = false,
            onClick = {}
        ),
        ConsultantFittingActionButtonState(
            label = "Примерка",
            fittingText = "12",
            showBadge = true,
            onClick = {}
        )
    )
}
