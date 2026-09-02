package ru.mercury.vpclient.features.messenger_sheet

import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
import ru.mercury.vpclient.shared.domain.usecase.BasketChatSendUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketChatSendUseCase.BasketChatSendException
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MessengerMessagesPagingDataUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val basketChatSendUseCase: BasketChatSendUseCase,
    messengerMessagesPagingDataUseCase: MessengerMessagesPagingDataUseCase
): ClientViewModel<MessengerIntent, MessengerModel, MessengerEvent>(MessengerModel()) {

    val messagesPagingFlow = messengerMessagesPagingDataUseCase(Unit).cachedIn(this)

    init {
        dispatch(MessengerIntent.CollectActiveEmployee)
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
            is MessengerIntent.MessageTextChange -> reduce { it.copy(messageText = intent.text) }
            is MessengerIntent.DismissClick -> {
                launch { MessengerEventManager.send(MessengerHostEvent.DismissRequest) }
            }
            is MessengerIntent.SendClick -> {
                val text = stateFlow.value.messageText.trim()
                if (text.isEmpty()) return
                reduce { it.copy(messageText = "") }
                launch {
                    val messageId = basketChatSendUseCase(text).getOrThrow() ?: return@launch
                    send(MessengerEvent.MessageSent(messageId = messageId))
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
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is BasketChatSendException -> {
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
