package ru.mercury.vpclient.shared.ui.components.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientNotificationEntity
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumChat30
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14

data class NotificationItemState(
    val notificationEntity: ClientNotificationEntity,
    val dateText: String,
    val onClick: () -> Unit
) {
    val isEnabled: Boolean
        get() = notificationEntity.deepLinkUrl.isNotBlank()

    val isBadgeVisible: Boolean
        get() = notificationEntity.badge > 0

    val isImageVisible: Boolean
        get() = notificationEntity.imageUrl.isNotBlank()
}

@Composable
fun NotificationItem(
    state: NotificationItemState,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(enabled = state.isEnabled, onClick = state.onClick)
            .alpha(if (state.isEnabled) 1F else .4F)
            .padding(horizontal = 16.dp)
    ) {
        val (mediaRef, titleRef, dateRef, messageRef, dividerRef) = createRefs()

        Row(
            modifier = Modifier.constrainAs(mediaRef) {
                width = Dimension.wrapContent
                height = Dimension.wrapContent
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.isBadgeVisible) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        )
                )
            }

            when {
                state.isImageVisible -> {
                    ClientAsyncImage(
                        imageUrl = state.notificationEntity.imageUrl,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width = 53.dp, height = 74.dp)
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier.size(width = 53.dp, height = 74.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = VipPlatinumChat30,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        Text(
            text = state.notificationEntity.title,
            modifier = Modifier.constrainAs(titleRef) {
                width = Dimension.fillToConstraints
                start.linkTo(mediaRef.end, 16.dp)
                top.linkTo(parent.top, 8.dp)
                end.linkTo(dateRef.start, 8.dp)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 15.sp,
                letterSpacing = .3.sp
            )
        )

        Text(
            text = state.dateText,
            modifier = Modifier.constrainAs(dateRef) {
                top.linkTo(titleRef.top)
                end.linkTo(parent.end)
                bottom.linkTo(titleRef.bottom)
            },
            maxLines = 1,
            style = MaterialTheme.typography.regular11.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 11.sp
            )
        )

        Text(
            text = state.notificationEntity.message,
            modifier = Modifier.constrainAs(messageRef) {
                start.linkTo(mediaRef.end, 16.dp)
                top.linkTo(titleRef.bottom, 2.dp)
                end.linkTo(parent.end)
                bottom.linkTo(dividerRef.top, 8.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .constrainAs(dividerRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun NotificationItemPreview(
    @PreviewParameter(NotificationItemStateProvider::class) state: NotificationItemState
) {
    NotificationItem(
        state = state
    )
}

private class NotificationItemStateProvider: PreviewParameterProvider<NotificationItemState> {
    override val values: Sequence<NotificationItemState> = sequenceOf(
        NotificationItemState(
            notificationEntity = ClientNotificationEntity(
                id = 1,
                category = ClientNotificationCategory.ALL,
                badge = 1,
                type = 0,
                imageUrl = "",
                title = "Новое сообщение",
                message = "Консультант отправил Вам новую подборку",
                deepLinkUrl = "vipplatinum://compilation/1",
                timestamp = "2026-07-21T10:30:00+03:00"
            ),
            dateText = "10:30",
            onClick = {}
        ),
        NotificationItemState(
            notificationEntity = ClientNotificationEntity(
                id = 2,
                category = ClientNotificationCategory.ALL,
                badge = 0,
                type = 0,
                imageUrl = "",
                title = "Новое сообщение",
                message = "Уведомление без бейджа",
                deepLinkUrl = "vipplatinum://compilation/2",
                timestamp = "2026-07-21T11:00:00+03:00"
            ),
            dateText = "11:00",
            onClick = {}
        ),
        NotificationItemState(
            notificationEntity = ClientNotificationEntity(
                id = 3,
                category = ClientNotificationCategory.CATALOGS_AND_ACTIONS,
                badge = 0,
                type = 0,
                imageUrl = "https://example.com/image.png",
                title = "Новая акция",
                message = "Уведомление с изображением без перехода",
                deepLinkUrl = "",
                timestamp = "2026-07-20T12:00:00+03:00"
            ),
            dateText = "Вчера",
            onClick = {}
        )
    )
}
