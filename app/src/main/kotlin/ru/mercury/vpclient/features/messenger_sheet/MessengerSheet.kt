@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.messenger_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.messenger_sheet.intent.MessengerIntent
import ru.mercury.vpclient.features.messenger_sheet.model.MessengerModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.messenger.MessageSendButton
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.ChevronDown24
import ru.mercury.vpclient.shared.ui.icons.Microphone24
import ru.mercury.vpclient.shared.ui.icons.Paperclip24
import ru.mercury.vpclient.shared.ui.icons.PhoneCalling22
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun MessengerSheet(
    viewModel: MessengerViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    MessengerSheetContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun MessengerSheetContent(
    state: MessengerModel,
    dispatch: (MessengerIntent) -> Unit
) {
    SharedModalBottomSheet(
        onDismissRequest = { dispatch(MessengerIntent.DismissClick) },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        SharedScaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ChevronDown24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error
                    )

                    CenterAlignedTopAppBar(
                        title = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
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
                        },
                        navigationIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .size(44.dp)
                                    .clip(CircleShape)
                            ) {
                                ClientAsyncImage(
                                    imageUrl = state.activeEmployeeEntity.previewPhotoUrl,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { dispatch(MessengerIntent.CallClick) },
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(44.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = PhoneCalling22,
                                        contentDescription = null,
                                        modifier = Modifier.size(22.dp),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        },
                        windowInsets = WindowInsets(left = 0.dp, right = 0.dp),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                            actionIconContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 16.dp, end = 8.dp, bottom = 8.dp)
                ) {
                    var textFieldLineCount by remember { mutableIntStateOf(1) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = state.messageText,
                            onValueChange = { text -> dispatch(MessengerIntent.MessageTextChange(text)) },
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
                                    if (state.messageText.isEmpty()) {
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

                        IconButton(
                            onClick = { dispatch(MessengerIntent.AttachClick) },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Paperclip24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (state.isSendButtonVisible) {
                            MessageSendButton(
                                onClick = { dispatch(MessengerIntent.SendClick) },
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            IconButton(
                                onClick = { dispatch(MessengerIntent.MicClick) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Microphone24,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
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
private fun MessengerSheetContentPreview(
    @PreviewParameter(MessengerModelPreviewParameterProvider::class) state: MessengerModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MessengerSheetContent(
            state = state,
            dispatch = {}
        )
    }
}

private class MessengerModelPreviewParameterProvider: PreviewParameterProvider<MessengerModel> {

    override val values: Sequence<MessengerModel> = sequenceOf(
        MessengerModel(
            activeEmployeeEntity = EmployeeEntity.Empty.copy(
                employeeName = "Светлана",
                employeeBrand = "Saint Laurent"
            ),
            messageText = ""
        ),
        MessengerModel(
            activeEmployeeEntity = EmployeeEntity.Empty.copy(
                employeeName = "Светлана",
                employeeBrand = "Saint Laurent"
            ),
            messageText = "Добрый день"
        )
    )
}
