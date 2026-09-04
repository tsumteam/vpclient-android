package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.ClearCircle24
import ru.mercury.vpclient.shared.ui.icons.EditOutline24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.grey
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular14

data class MessengerEditingBarState(
    val messageText: String,
    val onCloseClick: () -> Unit
)

@Composable
fun MessengerEditingBar(
    state: MessengerEditingBarState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = EditOutline24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Spacer(
            modifier = Modifier
                .size(width = 2.dp, height = 40.dp)
                .background(MaterialTheme.colorScheme.grey)
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            modifier = Modifier.weight(1F)
        ) {
            Text(
                text = stringResource(ClientStrings.MessengerEditing),
                style = MaterialTheme.typography.regular12.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.messageText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        IconButton(
            onClick = state.onCloseClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = ClearCircle24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.grey
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerEditingBarPreview(
    @PreviewParameter(MessengerEditingBarStatePreviewParameterProvider::class) state: MessengerEditingBarState
) {
    MessengerEditingBar(
        state = state
    )
}

private class MessengerEditingBarStatePreviewParameterProvider: PreviewParameterProvider<MessengerEditingBarState> {
    override val values: Sequence<MessengerEditingBarState> = sequenceOf(
        MessengerEditingBarState(
            messageText = "Подскажите статус заказа",
            onCloseClick = {}
        ),
        MessengerEditingBarState(
            messageText = "Светлана, здравствуйте! Вчера отправила заказ, но до сих пор не получила ответа по срокам доставки.",
            onCloseClick = {}
        )
    )
}
