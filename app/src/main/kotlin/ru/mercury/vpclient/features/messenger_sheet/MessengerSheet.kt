@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.messenger_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.messenger_sheet.intent.MessengerIntent
import ru.mercury.vpclient.features.messenger_sheet.model.MessengerModel
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.messenger.MessageInput
import ru.mercury.vpclient.shared.ui.components.messenger.MessageInputState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium17

@Composable
fun MessengerSheet(
    state: MessengerModel,
    dispatch: (MessengerIntent) -> Unit
) {
    var commentText by rememberSaveable { mutableStateOf("") }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(MessengerIntent.DismissClick) },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        SharedScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = state.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.livretMedium17.copy(
                                lineHeight = 26.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(MessengerIntent.DismissClick) }
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
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    MessageInput(
                        state = MessageInputState(
                            commentText = commentText,
                            onCommentChange = { comment -> commentText = comment },
                            onSendClick = { dispatch(MessengerIntent.SendClick(commentText)) }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerSheetPreview(
    @PreviewParameter(MessengerModelPreviewParameterProvider::class) state: MessengerModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MessengerSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class MessengerModelPreviewParameterProvider: PreviewParameterProvider<MessengerModel> {

    override val values: Sequence<MessengerModel> = sequenceOf(
        MessengerModel(
            name = "Катя",
            brand = "Brioni"
        )
    )
}
