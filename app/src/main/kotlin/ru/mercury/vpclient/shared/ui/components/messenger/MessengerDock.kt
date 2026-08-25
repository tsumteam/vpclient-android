package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.ChevronUp24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular15
import kotlin.math.min

data class MessengerDockState(
    val name: String,
    val brand: String,
    val onClick: () -> Unit
)

@Composable
fun MessengerDock(
    state: MessengerDockState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .shadow(
                elevation = 8.dp,
                shape = MessengerDockShape,
                clip = false
            )
            .clip(MessengerDockShape)
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = state.onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ChevronUp24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(.44F)
                .padding(horizontal = 4.dp)
                .height(24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Chat24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = state.name,
                modifier = Modifier.weight(1F, fill = false),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.medium14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 16.sp
                )
            )
        }

        Text(
            text = state.brand,
            maxLines = 1,
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

private val MessengerDockShape = GenericShape { size, _ ->
    val footTop = size.height * (40F / 84F)
    val centerWidth = size.width * .44F
    val centerStart = (size.width - centerWidth) / 2F
    val centerEnd = centerStart + centerWidth
    val radius = min(size.height * .1F, size.width * .04F)
    moveTo(0F, size.height)
    lineTo(0F, footTop)
    lineTo(centerStart, footTop)
    lineTo(centerStart, radius)
    quadraticTo(centerStart, 0F, centerStart + radius, 0F)
    lineTo(centerEnd - radius, 0F)
    quadraticTo(centerEnd, 0F, centerEnd, radius)
    lineTo(centerEnd, footTop)
    lineTo(size.width, footTop)
    lineTo(size.width, size.height)
    close()
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerDockPreview(
    @PreviewParameter(MessengerDockPreviewParameterProvider::class) state: MessengerDockState
) {
    MessengerDock(
        state = state
    )
}

private class MessengerDockPreviewParameterProvider: PreviewParameterProvider<MessengerDockState> {

    override val values: Sequence<MessengerDockState> = sequenceOf(
        MessengerDockState(
            name = "Катя",
            brand = "Brioni",
            onClick = {}
        ),
        MessengerDockState(
            name = "Екатеринищещещещеще",
            brand = "Brioni",
            onClick = {}
        ),
        MessengerDockState(
            name = "",
            brand = "",
            onClick = {}
        )
    )
}
