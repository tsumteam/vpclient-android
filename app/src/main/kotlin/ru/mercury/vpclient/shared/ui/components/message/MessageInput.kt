package ru.mercury.vpclient.shared.ui.components.message

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular15

data class MessageInputState(
    val commentText: String,
    val onCommentChange: (String) -> Unit,
    val onSendClick: () -> Unit
)

@Composable
fun MessageInput(
    state: MessageInputState,
    modifier: Modifier = Modifier
) {
    var textFieldLineCount by remember { mutableIntStateOf(1) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            value = state.commentText,
            onValueChange = state.onCommentChange,
            modifier = Modifier
                .weight(1F)
                .heightIn(min = 48.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(if (textFieldLineCount > 1) 16.dp else 50.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            minLines = 1,
            maxLines = 5,
            onTextLayout = { textLayoutResult ->
                textFieldLineCount = textLayoutResult.lineCount
            },
            textStyle = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 19.sp,
                letterSpacing = .2.sp
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.commentText.isEmpty()) {
                        Text(
                            text = stringResource(ClientStrings.DetailsMessageCommentPlaceholder),
                            style = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }

                    innerTextField()
                }
            }
        )

        MessageSendButton(
            onClick = state.onSendClick
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessageInputPreview(
    @PreviewParameter(MessageInputStateProvider::class) state: MessageInputState
) {
    MessageInput(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

private class MessageInputStateProvider: PreviewParameterProvider<MessageInputState> {

    override val values: Sequence<MessageInputState> = sequenceOf(
        MessageInputState(
            commentText = "",
            onCommentChange = {},
            onSendClick = {}
        ),
        MessageInputState(
            commentText = "Комментарий",
            onCommentChange = {},
            onSendClick = {}
        ),
        MessageInputState(
            commentText = "Первая строка\nВторая строка\nТретья строка\nЧетвертая строка",
            onCommentChange = {},
            onSendClick = {}
        )
    )
}
