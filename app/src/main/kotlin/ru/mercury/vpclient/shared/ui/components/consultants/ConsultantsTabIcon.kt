package ru.mercury.vpclient.shared.ui.components.consultants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.R
import ru.mercury.vpclient.shared.ui.components.BadgeText
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class ConsultantsTabIconState(
    val selected: Boolean,
    val badgeText: String?
) {
    val isBadgeVisible: Boolean
        get() = !badgeText.isNullOrEmpty()
}

@Composable
fun ConsultantsTabIcon(
    state: ConsultantsTabIconState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                when {
                    state.selected -> R.drawable.ic_consultants_active_24
                    else -> R.drawable.ic_consultants_inactive_24
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )

        if (state.isBadgeVisible) {
            BadgeText(
                text = state.badgeText.orEmpty(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 15.dp, bottom = 13.dp)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ConsultantsTabIconPreview(
    @PreviewParameter(ConsultantsTabIconStateProvider::class) state: ConsultantsTabIconState
) {
    ConsultantsTabIcon(
        state = state
    )
}

private class ConsultantsTabIconStateProvider: PreviewParameterProvider<ConsultantsTabIconState> {

    override val values: Sequence<ConsultantsTabIconState> = sequenceOf(
        ConsultantsTabIconState(
            selected = false,
            badgeText = null
        ),
        ConsultantsTabIconState(
            selected = true,
            badgeText = "1"
        ),
        ConsultantsTabIconState(
            selected = false,
            badgeText = "11"
        ),
        ConsultantsTabIconState(
            selected = true,
            badgeText = "111"
        )
    )
}
