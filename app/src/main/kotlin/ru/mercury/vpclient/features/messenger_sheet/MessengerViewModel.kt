package ru.mercury.vpclient.features.messenger_sheet

import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEvent
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEventManager
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerHostEvent
import ru.mercury.vpclient.features.messenger_sheet.intent.MessengerIntent
import ru.mercury.vpclient.features.messenger_sheet.model.MessengerModel
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.BasketChatDeleteUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketChatDeleteUseCase.BasketChatDeleteException
import ru.mercury.vpclient.shared.domain.usecase.BasketChatEditUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketChatEditUseCase.BasketChatEditException
import ru.mercury.vpclient.shared.domain.usecase.BasketChatGetUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketChatSendUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketChatSendUseCase.BasketChatSendException
import ru.mercury.vpclient.shared.domain.usecase.BasketChatSendUseCase.Params
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MessengerMessagesPagingDataUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val basketChatGetUseCase: BasketChatGetUseCase,
    private val basketChatSendUseCase: BasketChatSendUseCase,
    private val basketChatDeleteUseCase: BasketChatDeleteUseCase,
    private val basketChatEditUseCase: BasketChatEditUseCase,
    messengerMessagesPagingDataUseCase: MessengerMessagesPagingDataUseCase
): ClientViewModel<MessengerIntent, MessengerModel, MessengerEvent>(MessengerModel()) {

    val messagesPagingFlow = messengerMessagesPagingDataUseCase(Unit).cachedIn(this)

    init {
        dispatch(MessengerIntent.CollectActiveEmployee)
        dispatch(MessengerIntent.PollNewMessages)
    }

    override fun dispatch(intent: MessengerIntent) {
        when (intent) {
            is MessengerIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { entity -> reduce { it.copy(activeEmployeeEntity = entity) } }
                }
            }
            is MessengerIntent.PollNewMessages -> {
                launch {
                    while (isActive) {
                        try {
                            basketChatGetUseCase(Unit).getOrThrow()
                        } catch (throwable: Throwable) {
                            if (throwable is CancellationException) throw throwable
                        }
                        delay(BasketChatGetUseCase.POLL_INTERVAL_MILLIS.milliseconds)
                    }
                }
            }
            is MessengerIntent.MessageTextChange -> reduce { it.copy(messageText = intent.text) }
            is MessengerIntent.DismissClick -> {
                launch { MessengerEventManager.send(MessengerHostEvent.DismissRequest) }
            }
            is MessengerIntent.SendClick -> {
                val text = stateFlow.value.messageText.trim()
                if (text.isEmpty()) return
                when (val editingId = stateFlow.value.editingMessageId) {
                    null -> {
                        val replyId = stateFlow.value.replyMessageId
                        val replyText = stateFlow.value.replyText
                        reduce {
                            it.copy(
                                messageText = "",
                                replyMessageId = null,
                                replyAuthorName = "",
                                replyText = ""
                            )
                        }
                        launch {
                            val messageId = basketChatSendUseCase(
                                Params(text = text, citatedMessageId = replyId, citation = replyId?.let { replyText })
                            ).getOrThrow() ?: return@launch
                            send(MessengerEvent.MessageSent(messageId = messageId))
                        }
                    }
                    else -> {
                        reduce { it.copy(messageText = "", editingMessageId = null, editingOriginalText = "") }
                        launch {
                            basketChatEditUseCase(BasketChatEditUseCase.Params(messageId = editingId, text = text)).getOrThrow()
                        }
                    }
                }
            }
            is MessengerIntent.CallClick -> {
                launch { send(MessengerEvent.LaunchDialer(stateFlow.value.activeEmployeeEntity.employeePhone)) }
            }
            is MessengerIntent.AttachClick -> reduce { it.copy(isAttachSheetVisible = true) }
            is MessengerIntent.DismissAttachSheet -> reduce { it.copy(isAttachSheetVisible = false) }
            is MessengerIntent.AttachGalleryClick -> {
                reduce { it.copy(isAttachSheetVisible = false) }
                // fixme Выбор фото/видео из галереи пока не реализован
            }
            is MessengerIntent.AttachCartProductsClick -> {
                reduce { it.copy(isAttachSheetVisible = false) }
                // fixme Выбор товаров из корзины/примерки пока не реализован
            }
            is MessengerIntent.AttachCatalogClick -> {
                reduce { it.copy(isAttachSheetVisible = false) }
                // fixme Выбор товаров из каталога пока не реализован
            }
            is MessengerIntent.MicClick -> {
                // fixme Запись голосового сообщения пока не реализована
                return
            }
            is MessengerIntent.ProductClick -> {
                if (intent.productId.isBlank()) return
                launch {
                    MessengerEventManager.send(MessengerHostEvent.DismissRequest)
                    MainEventManager.send(DetailsRoute(id = intent.productId, openedFromCart = true))
                }
            }
            is MessengerIntent.ReplyMessageClick -> {
                reduce {
                    it.copy(
                        messageText = if (it.editingMessageId != null) "" else it.messageText,
                        replyMessageId = intent.messageId,
                        replyAuthorName = intent.authorName,
                        replyText = intent.text,
                        editingMessageId = null,
                        editingOriginalText = ""
                    )
                }
            }
            is MessengerIntent.CopyMessageClick -> {
                launch { send(MessengerEvent.CopyMessageText(intent.text)) }
            }
            is MessengerIntent.EditMessageClick -> {
                reduce {
                    it.copy(
                        messageText = intent.text,
                        editingMessageId = intent.messageId,
                        editingOriginalText = intent.text,
                        replyMessageId = null,
                        replyAuthorName = "",
                        replyText = ""
                    )
                }
            }
            is MessengerIntent.CancelEditClick -> {
                reduce { it.copy(messageText = "", editingMessageId = null, editingOriginalText = "") }
            }
            is MessengerIntent.CancelReplyClick -> {
                reduce { it.copy(replyMessageId = null, replyAuthorName = "", replyText = "") }
            }
            is MessengerIntent.DeleteMessageClick -> {
                launch { basketChatDeleteUseCase(intent.messageId).getOrThrow() }
            }
            is MessengerIntent.ResendMessageClick -> Unit
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is BasketChatSendException -> {
                launch { send(MessengerEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is BasketChatDeleteException -> {
                launch { send(MessengerEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is BasketChatEditException -> {
                launch { send(MessengerEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(MessengerEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(MessengerEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
