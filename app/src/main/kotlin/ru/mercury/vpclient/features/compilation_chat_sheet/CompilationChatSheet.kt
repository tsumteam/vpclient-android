@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilation_chat_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.compilation_chat_sheet.intent.CompilationChatIntent
import ru.mercury.vpclient.features.compilation_chat_sheet.model.CompilationChatModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationChatCard
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationChatCardState
import ru.mercury.vpclient.shared.ui.components.message.MessageInput
import ru.mercury.vpclient.shared.ui.components.message.MessageInputState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

@Composable
fun CompilationChatSheet(
    state: CompilationChatModel,
    dispatch: (CompilationChatIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var commentText by rememberSaveable { mutableStateOf(state.commentText) }
    val sheetDispatch: (CompilationChatIntent) -> Unit = { intent ->
        when (intent) {
            is CompilationChatIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is CompilationChatIntent.CommentChange -> commentText = intent.comment
            is CompilationChatIntent.SendClick -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { sheetDispatch(CompilationChatIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        val inlinedState = state.copy(commentText = commentText)

        Column(
            modifier = Modifier.imePadding()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CompilationChatSheetTitle),
                        style = MaterialTheme.typography.livretMedium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CompilationChatIntent.DismissRequest) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

            CompilationChatCard(
                state = CompilationChatCardState(
                    entity = inlinedState.compilationEntity,
                    selectedLookTitle = inlinedState.selectedLookTitle
                ),
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 14.dp)
            )

            MessageInput(
                state = MessageInputState(
                    commentText = commentText,
                    onCommentChange = { comment -> sheetDispatch(CompilationChatIntent.CommentChange(comment)) },
                    onSendClick = { sheetDispatch(CompilationChatIntent.SendClick(commentText)) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CompilationChatSheetPreview(
    @PreviewParameter(CompilationChatSheetModelProvider::class) state: CompilationChatModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CompilationChatSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CompilationChatSheetModelProvider:
    PreviewParameterProvider<CompilationChatModel> {

    override val values: Sequence<CompilationChatModel> = sequenceOf(
        CompilationChatModel(
            compilationEntity = CompilationEntity.Empty.copy(
                id = 1,
                photoUrl = "",
                name = "BLV/Hotel"
            )
        )
    )
}
