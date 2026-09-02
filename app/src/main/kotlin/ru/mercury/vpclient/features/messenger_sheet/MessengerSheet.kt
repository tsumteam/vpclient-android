@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.messenger_sheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.messenger_attach_sheet.MessengerAttachSheet
import ru.mercury.vpclient.features.messenger_attach_sheet.intent.MessengerAttachIntent
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEvent
import ru.mercury.vpclient.features.messenger_sheet.intent.MessengerIntent
import ru.mercury.vpclient.features.messenger_sheet.model.MessengerModel
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayload
import ru.mercury.vpclient.shared.data.entity.MessengerMessagePayloadType
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadImage
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadProduct
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.ui.components.PagingFailureBox
import ru.mercury.vpclient.shared.ui.components.PagingLoadingBox
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.messenger.MessageSendButton
import ru.mercury.vpclient.shared.ui.components.messenger.MessengerMessage
import ru.mercury.vpclient.shared.ui.components.messenger.MessengerMessagePlaceholder
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.ChevronDown24
import ru.mercury.vpclient.shared.ui.icons.Microphone24
import ru.mercury.vpclient.shared.ui.icons.Paperclip24
import ru.mercury.vpclient.shared.ui.icons.PhoneCalling22
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.isPagingFailure
import ru.mercury.vpclient.shared.ui.ktx.isPagingLoading
import ru.mercury.vpclient.shared.ui.ktx.isRefreshFailure
import ru.mercury.vpclient.shared.ui.ktx.isRefreshLoading
import ru.mercury.vpclient.shared.ui.ktx.launcherDialer
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun MessengerSheet(
    viewModel: MessengerViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pagingItems = viewModel.messagesPagingFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    var lastSentMessageId by remember { mutableStateOf<Long?>(null) }
    var sentMessageIdToAnimate by remember { mutableStateOf<Long?>(null) }
    var isScrolledToBottom by remember { mutableStateOf(true) }

    LaunchedEffect(lastSentMessageId) {
        val sentMessageId = lastSentMessageId ?: return@LaunchedEffect
        snapshotFlow {
            pagingItems.itemSnapshotList.items.indexOfFirst { message -> message.id == sentMessageId }
        }.first { index -> index >= 0 }

        lazyListState.requestScrollToItem(0)
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }.collect { atBottom -> isScrolledToBottom = atBottom }
    }

    LaunchedEffect(Unit) {
        var isInitialTopMessage = true
        snapshotFlow { pagingItems.itemSnapshotList.items.firstOrNull()?.id }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { topMessageId ->
                if (isInitialTopMessage) {
                    isInitialTopMessage = false
                    return@collect
                }
                if (topMessageId == lastSentMessageId) return@collect

                val wasAtBottom = isScrolledToBottom || lazyListState.firstVisibleItemIndex <= 1
                if (wasAtBottom) lazyListState.animateScrollToItem(0)
            }
    }

    MessengerSheetContent(
        state = state,
        pagingItems = pagingItems,
        lazyListState = lazyListState,
        sentMessageIdToAnimate = sentMessageIdToAnimate,
        onSentMessageAnimationFinished = { messageId ->
            if (sentMessageIdToAnimate == messageId) sentMessageIdToAnimate = null
        },
        dispatch = viewModel::dispatch,
        snackbarHostState = snackbarHostState
    )

    if (state.isAttachSheetVisible) {
        MessengerAttachSheet(
            dispatch = { intent ->
                when (intent) {
                    is MessengerAttachIntent.DismissClick -> {
                        viewModel.dispatch(MessengerIntent.DismissAttachSheet)
                    }
                    is MessengerAttachIntent.GalleryClick -> {
                        viewModel.dispatch(MessengerIntent.AttachGalleryClick)
                    }
                    is MessengerAttachIntent.CartProductsClick -> {
                        viewModel.dispatch(MessengerIntent.AttachCartProductsClick)
                    }
                    is MessengerAttachIntent.CatalogClick -> {
                        viewModel.dispatch(MessengerIntent.AttachCatalogClick)
                    }
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is MessengerEvent.MessageSent -> {
                scope.launch {
                    if (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0) {
                        lazyListState.animateScrollToItem(0)
                    }
                    lastSentMessageId = event.messageId
                    sentMessageIdToAnimate = event.messageId
                    pagingItems.refresh()
                }
            }
            is MessengerEvent.LaunchDialer -> context.launcherDialer(event.phone)
            is MessengerEvent.SnackbarErrorMessage -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun MessengerSheetContent(
    state: MessengerModel,
    pagingItems: LazyPagingItems<MessengerMessageEntity>,
    lazyListState: LazyListState,
    sentMessageIdToAnimate: Long?,
    onSentMessageAnimationFinished: (Long) -> Unit,
    dispatch: (MessengerIntent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    SharedModalBottomSheet(
        onDismissRequest = { dispatch(MessengerIntent.DismissClick) },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        sheetGesturesEnabled = true
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
                        .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
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
                                            text = stringResource(ClientStrings.MessengerMessagePlaceholder),
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

                        AnimatedContent(
                            targetState = state.isSendButtonVisible,
                            modifier = Modifier.size(40.dp),
                            transitionSpec = {
                                (fadeIn() + scaleIn(initialScale = .8F)) togetherWith
                                    (fadeOut() + scaleOut(targetScale = .8F))
                            },
                            label = "messenger_action_button_animation"
                        ) { isSendButtonVisible ->
                            if (isSendButtonVisible) {
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
            },
            snackbarHost = {
                SharedSnackbarHost(
                    hostState = snackbarHostState,
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        ) { innerPadding ->
            val isInitialLoading = pagingItems.isRefreshLoading && pagingItems.itemCount == 0

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true,
                contentPadding = innerPadding + PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = !isInitialLoading
            ) {
                if (isInitialLoading) {
                    items(
                        count = 8
                    ) { index ->
                        MessengerMessagePlaceholder(
                            direction = if (index % 2 == 0) {
                                MessengerMessageDirection.Incoming
                            } else {
                                MessengerMessageDirection.Outgoing
                            }
                        )
                    }
                }

                if (!isInitialLoading) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { message -> message.id },
                        contentType = pagingItems.itemContentType()
                    ) { index ->
                        val message = pagingItems[index]

                        if (message != null) {
                            if (message.id == sentMessageIdToAnimate) {
                                val visibleState = remember(message.id) {
                                    MutableTransitionState(false).apply { targetState = true }
                                }

                                LaunchedEffect(visibleState) {
                                    snapshotFlow { visibleState.isIdle && visibleState.currentState }
                                        .first { isAnimationFinished -> isAnimationFinished }
                                    onSentMessageAnimationFinished(message.id)
                                }

                                AnimatedVisibility(
                                    visibleState = visibleState,
                                    enter = expandVertically(
                                        animationSpec = tween(
                                            durationMillis = 320,
                                            easing = FastOutSlowInEasing
                                        ),
                                        expandFrom = Alignment.Bottom,
                                        clip = false
                                    ) + slideInVertically(
                                        animationSpec = tween(
                                            durationMillis = 320,
                                            easing = FastOutSlowInEasing
                                        ),
                                        initialOffsetY = { fullHeight -> fullHeight }
                                    ) + fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 320,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                ) {
                                    MessengerMessage(
                                        message = message,
                                        onProductClick = { productId -> dispatch(MessengerIntent.ProductClick(productId)) }
                                    )
                                }
                            } else {
                                MessengerMessage(
                                    message = message,
                                    onProductClick = { productId -> dispatch(MessengerIntent.ProductClick(productId)) }
                                )
                            }
                        }
                    }

                    when {
                        pagingItems.isPagingLoading -> {
                            item {
                                PagingLoadingBox(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp)
                                )
                            }
                        }
                        pagingItems.isPagingFailure -> {
                            item {
                                PagingFailureBox(
                                    onClick = pagingItems::retry,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp)
                                )
                            }
                        }
                    }
                }

                if (pagingItems.isRefreshFailure) {
                    item {
                        PagingFailureBox(
                            onClick = pagingItems::retry,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerSheetContentPreview(
    @PreviewParameter(MessengerSheetPreviewParameterProvider::class) state: MessengerSheetPreviewState
) {
    val pagingItems = remember(state) {
        MutableStateFlow(
            PagingData.from(
                data = state.messages,
                sourceLoadStates = state.sourceLoadStates
            )
        )
    }.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MessengerSheetContent(
            state = state.state,
            pagingItems = pagingItems,
            lazyListState = rememberLazyListState(),
            sentMessageIdToAnimate = null,
            onSentMessageAnimationFinished = {},
            dispatch = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

private data class MessengerSheetPreviewState(
    val state: MessengerModel,
    val messages: List<MessengerMessageEntity>,
    val sourceLoadStates: LoadStates
)

private class MessengerSheetPreviewParameterProvider: PreviewParameterProvider<MessengerSheetPreviewState> {

    private val previewModel = MessengerModel(
        activeEmployeeEntity = EmployeeEntity.Empty.copy(
            employeeName = "Светлана",
            employeeBrand = "Saint Laurent"
        ),
        messageText = ""
    )
    private val idleLoadStates = LoadStates(
        refresh = LoadState.NotLoading(endOfPaginationReached = false),
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
    )
    private val loadingLoadStates = LoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
    )
    private val previewMessages = listOf(
        MessengerMessageEntity(
            id = 6,
            createTime = "2026-08-26T16:44:00+03:00",
            text = "Подскажите, пожалуйста, когда планируется доставка?",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Read,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 5,
            createTime = "2026-08-26T16:43:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Product,
                products = listOf(
                    MessengerPayloadProduct(
                        id = "0000",
                        brand = "SAINT LAURENT",
                        name = "Кожаная куртка oversize",
                        itemId = "0000000",
                        price = 189_900.0,
                        colorId = "BLK",
                        colorName = "Чёрный",
                        imageUrl = "https://example.com/product.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 4,
            createTime = "2026-08-26T16:42:00+03:00",
            text = "",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Sent,
            isEdited = false,
            payload = MessengerMessagePayload(
                type = MessengerMessagePayloadType.Images,
                images = listOf(
                    MessengerPayloadImage(
                        imageUrl = "https://example.com/photo-1.jpg",
                        previewUrl = "https://example.com/photo-1.jpg"
                    )
                )
            )
        ),
        MessengerMessageEntity(
            id = 3,
            createTime = "2026-08-26T16:20:00+03:00",
            text = "Заказ №1234567 оформлен",
            direction = MessengerMessageDirection.System,
            status = null,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 2,
            createTime = "2026-08-26T16:28:00+03:00",
            text = "Мария, здравствуйте, прошу прощения, заказ скоро будет сформирован.",
            direction = MessengerMessageDirection.Incoming,
            status = null,
            isEdited = false
        ),
        MessengerMessageEntity(
            id = 1,
            createTime = "2026-08-26T16:40:00+03:00",
            text = "Светлана, здравствуйте! Вчера отправила заказ, но до сих пор не получила ответа.",
            direction = MessengerMessageDirection.Outgoing,
            status = MessengerMessageStatus.Read,
            isEdited = false
        )
    )

    override val values: Sequence<MessengerSheetPreviewState> = sequenceOf(
        MessengerSheetPreviewState(
            state = previewModel,
            messages = emptyList(),
            sourceLoadStates = loadingLoadStates
        ),
        MessengerSheetPreviewState(
            state = previewModel,
            messages = previewMessages,
            sourceLoadStates = idleLoadStates
        )
    )
}
