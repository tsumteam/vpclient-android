package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayload
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun MessengerTextMessage(
    message: MessengerMessageEntity,
    modifier: Modifier = Modifier
) {
    val isOutgoing = message.direction == MessengerMessageDirection.Outgoing

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 328.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = if (isOutgoing) 16.dp else 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isOutgoing) 0.dp else 16.dp,
                        bottomStart = 16.dp
                    )
                )
                .background(
                    when {
                        isOutgoing -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (message.payload?.citation.orEmpty().isNotEmpty()) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(
                                when {
                                    isOutgoing -> MaterialTheme.colorScheme.background.copy(alpha = .6F)
                                    else -> MaterialTheme.colorScheme.outline
                                }
                            )
                    )

                    Text(
                        text = message.payload?.citation.orEmpty(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular14.copy(
                            color = when {
                                isOutgoing -> MaterialTheme.colorScheme.background.copy(alpha = .6F)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }

            Text(
                text = message.text,
                style = MaterialTheme.typography.regular14.copy(
                    color = when {
                        isOutgoing -> MaterialTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.onBackground
                    },
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        MessengerMessageMeta(
            state = MessengerMessageMetaState(
                createTime = message.createTime,
                isEdited = message.isEdited,
                status = message.status
            )
        )
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
        ),
        MessengerMessageEntity(
            id = 4,
            createTime = "2026-08-26T16:30:00+03:00",
            text = "Здравствуйте! Заказ уже собран и завтра будет передан курьеру.",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                citation = "Светлана, здравствуйте! Вчера отправила заказ, но до сих пор не получила ответа."
            )
        ),
        MessengerMessageEntity(
            id = 5,
            createTime = "2026-08-26T16:32:00+03:00",
            text = "Да, всё верно, оформляйте доставку на этот адрес.",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Read,
            isEdited = false,
            payload = MessengerMessagePayload(
                citation = "Светлана, здравствуйте! Вчера отправила заказ, но до сих пор не получила ответа."
            )
        )
    )
}
