package ru.mercury.vpclient.features.messenger_sheet

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
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MessengerMessageEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MessengerMessagesUseCase
import ru.mercury.vpclient.shared.domain.usecase.MessengerMessagesUseCase.MessengerMessagesException
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val messengerMessageEntitiesFlowUseCase: MessengerMessageEntitiesFlowUseCase,
    private val messengerMessagesUseCase: MessengerMessagesUseCase
): ClientViewModel<MessengerIntent, MessengerModel, MessengerEvent>(MessengerModel()) {

    init {
        dispatch(MessengerIntent.CollectActiveEmployee)
        dispatch(MessengerIntent.CollectMessages)
        dispatch(MessengerIntent.LoadMessages)
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
            is MessengerIntent.CollectMessages -> {
                launch {
                    messengerMessageEntitiesFlowUseCase(Unit).collectLatest { entities ->
                        reduce { it.copy(messageEntities = entities) }
                    }
                }
            }
            is MessengerIntent.MessageTextChange -> reduce { it.copy(messageText = intent.text) }
            is MessengerIntent.LoadMessages -> {
                if (stateFlow.value.messagesJob?.isActive == true) return
                val job = launch {
                    messengerMessagesUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(messagesJob = null) } }
                }
                reduce { it.copy(messagesJob = job) }
            }
            is MessengerIntent.DismissClick -> {
                launch { MessengerEventManager.send(MessengerHostEvent.DismissRequest) }
            }
            is MessengerIntent.SendClick -> return // fixme
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
            is MessengerMessagesException -> {
                reduce { it.copy(messagesJob = null) }
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
