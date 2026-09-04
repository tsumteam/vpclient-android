package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.domain.mapper.messengerMessageDateText
import ru.mercury.vpclient.shared.ui.icons.MessengerCheck14x10
import ru.mercury.vpclient.shared.ui.icons.MessengerChecks20x10
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.blue
import ru.mercury.vpclient.shared.ui.theme.regular12

data class MessengerMessageMetaState(
    val createTime: String,
    val isEdited: Boolean,
    val status: MessengerMessageStatus?,
    val onClick: () -> Unit = {}
) {
    val isIconStatusVisible: Boolean
        get() = status != null && status != MessengerMessageStatus.Failed
}

@Composable
fun MessengerMessageMeta(
    state: MessengerMessageMetaState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickableWithoutRipple { state.onClick() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildString {
                if (state.isEdited) {
                    append(stringResource(ClientStrings.MessengerMessageEdited))
                }
                append(state.createTime.messengerMessageDateText())
            },
            style = MaterialTheme.typography.regular12.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp,
                letterSpacing = .2.sp
            )
        )

        if (state.isIconStatusVisible) {
            Icon(
                imageVector = when (requireNotNull(state.status)) {
                    MessengerMessageStatus.Sent -> MessengerCheck14x10
                    MessengerMessageStatus.Received,
                    MessengerMessageStatus.Read -> MessengerChecks20x10
                    MessengerMessageStatus.Failed -> MessengerCheck14x10
                },
                contentDescription = null,
                tint = when (state.status) {
                    MessengerMessageStatus.Read -> MaterialTheme.colorScheme.blue
                    MessengerMessageStatus.Sent,
                    MessengerMessageStatus.Received,
                    MessengerMessageStatus.Failed -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerMessageMetaPreview(
    @PreviewParameter(MessengerMessageMetaStatePreviewParameterProvider::class) state: MessengerMessageMetaState
) {
    MessengerMessageMeta(
        state = state
    )
}

private class MessengerMessageMetaStatePreviewParameterProvider: PreviewParameterProvider<MessengerMessageMetaState> {
    override val values: Sequence<MessengerMessageMetaState> = sequenceOf(
        MessengerMessageMetaState(
            createTime = "2026-08-26T16:40:00+03:00",
            isEdited = false,
            status = MessengerMessageStatus.Read
        ),
        MessengerMessageMetaState(
            createTime = "2026-08-26T16:40:00+03:00",
            isEdited = true,
            status = MessengerMessageStatus.Sent
        ),
        MessengerMessageMetaState(
            createTime = "2026-08-26T16:40:00+03:00",
            isEdited = false,
            status = null
        )
    )
}
