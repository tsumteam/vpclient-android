package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.domain.mapper.messengerMessageDateText
import ru.mercury.vpclient.shared.ui.icons.MessengerCheck14x10
import ru.mercury.vpclient.shared.ui.icons.MessengerChecks20x10
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.blue
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun MessengerTextMessage(
    message: MessengerMessageEntity,
    modifier: Modifier = Modifier
) {
    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 328.dp),
            shape = RoundedCornerShape(
                topStart = 17.dp,
                topEnd = 17.dp,
                bottomEnd = if (isOutgoing) 0.dp else 17.dp,
                bottomStart = if (isOutgoing) 17.dp else 0.dp
            ),
            color = if (isOutgoing) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.regular15.copy(
                    color = if (isOutgoing) {
                        MaterialTheme.colorScheme.background
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Row(
            modifier = Modifier.padding(top = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildString {
                    if (message.isEdited) append("Изменено ")
                    append(message.createTime.messengerMessageDateText())
                },
                style = MaterialTheme.typography.regular12.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            )

            message.status?.let { status ->
                Icon(
                    imageVector = when (status) {
                        MessengerMessageStatus.Sent -> MessengerCheck14x10
                        MessengerMessageStatus.Received, MessengerMessageStatus.Read -> MessengerChecks20x10
                    },
                    contentDescription = null,
                    tint = when (status) {
                        MessengerMessageStatus.Read -> MaterialTheme.colorScheme.blue
                        MessengerMessageStatus.Sent, MessengerMessageStatus.Received -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerTextMessagePreview(
    @PreviewParameter(MessengerTextMessagePreviewParameterProvider::class) message: MessengerMessageEntity
) {
    MessengerTextMessage(
        message = message
    )
}

private class MessengerTextMessagePreviewParameterProvider: PreviewParameterProvider<MessengerMessageEntity> {
    override val values: Sequence<MessengerMessageEntity> = sequenceOf(
        MessengerMessageEntity(
            id = 1,
            createTime = "2026-08-26T16:40:00+03:00",
            text = "Светлана, здравствуйте! Вчера отправила заказ, но до сих пор не получила никакого ответа.",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Read,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 2,
            createTime = "2026-08-26T16:38:00+03:00",
            text = "Уточните, пожалуйста, номер заказа.",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 3,
            createTime = "2026-08-26T16:28:00+03:00",
            text = "Мария, здравствуйте, прошу прощения, заказ скоро будет сформирован.",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false
        )
    )
}
