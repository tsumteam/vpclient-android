@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.ui.icons.Copy24
import ru.mercury.vpclient.shared.ui.icons.Delete24
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.icons.Repeat24
import ru.mercury.vpclient.shared.ui.icons.Return24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular16

data class MessengerMessageDropdownMenuState(
    val direction: MessengerMessageDirection,
    val isReplyable: Boolean,
    val isCopyable: Boolean,
    val isEditable: Boolean,
    val isDeletable: Boolean,
    val isResendable: Boolean,
    val onReplyClick: () -> Unit,
    val onCopyClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onDeleteClick: () -> Unit,
    val onResendClick: () -> Unit
)

@Composable
fun MessengerMessageDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    state: MessengerMessageDropdownMenuState,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = 224.dp),
        offset = if (state.direction == MessengerMessageDirection.Outgoing) {
            DpOffset(x = 8.dp, y = 0.dp)
        } else {
            DpOffset(x = (-8).dp, y = 0.dp)
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 2.dp
    ) {
        if (state.isReplyable) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(ClientStrings.MessengerMessageReply),
                        style = MaterialTheme.typography.regular16.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 20.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Return24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = state.onReplyClick
            )
        }

        if (state.isResendable) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(ClientStrings.MessengerMessageResend),
                        style = MaterialTheme.typography.regular16.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 20.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Repeat24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = state.onResendClick
            )
        }

        if (state.isCopyable) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(ClientStrings.MessengerMessageCopy),
                        style = MaterialTheme.typography.regular16.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 20.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Copy24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = state.onCopyClick
            )
        }

        if (state.isEditable) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(ClientStrings.MessengerMessageEdit),
                        style = MaterialTheme.typography.regular16.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 20.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Edit24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = state.onEditClick
            )
        }

        if (state.isDeletable) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(ClientStrings.MessengerMessageDelete),
                        style = MaterialTheme.typography.regular16.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 20.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Delete24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                onClick = state.onDeleteClick
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerMessageDropdownMenuPreview(
    @PreviewParameter(MessengerMessageDropdownMenuStatePreviewParameterProvider::class) state: MessengerMessageDropdownMenuState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MessengerMessageDropdownMenu(
            expanded = true,
            onDismissRequest = {},
            state = state
        )
    }
}

private class MessengerMessageDropdownMenuStatePreviewParameterProvider:
    PreviewParameterProvider<MessengerMessageDropdownMenuState> {

    private val empty = MessengerMessageDropdownMenuState(
        direction = MessengerMessageDirection.Incoming,
        isReplyable = false,
        isCopyable = false,
        isEditable = false,
        isDeletable = false,
        isResendable = false,
        onReplyClick = {},
        onCopyClick = {},
        onEditClick = {},
        onDeleteClick = {},
        onResendClick = {}
    )

    override val values: Sequence<MessengerMessageDropdownMenuState> = sequenceOf(
        empty.copy(
            direction = MessengerMessageDirection.Incoming,
            isReplyable = true,
            isCopyable = true
        ),
        empty.copy(
            direction = MessengerMessageDirection.Outgoing,
            isReplyable = true,
            isCopyable = true,
            isEditable = true,
            isDeletable = true
        ),
        empty.copy(
            direction = MessengerMessageDirection.Incoming,
            isReplyable = true,
            isCopyable = false
        ),
        empty.copy(
            direction = MessengerMessageDirection.Outgoing,
            isReplyable = false,
            isCopyable = false,
            isDeletable = true,
            isResendable = true
        )
    )
}
