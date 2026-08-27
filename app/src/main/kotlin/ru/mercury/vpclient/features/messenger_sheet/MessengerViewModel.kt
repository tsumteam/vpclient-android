package ru.mercury.vpclient.features.messenger_sheet

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEvent
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEventManager
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerHostEvent
import ru.mercury.vpclient.features.messenger_sheet.intent.MessengerIntent
import ru.mercury.vpclient.features.messenger_sheet.model.MessengerModel
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<MessengerIntent, MessengerModel, MessengerEvent>(MessengerModel()) {

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
            is MessengerIntent.MessageTextChange -> {
                reduce { it.copy(messageText = intent.text) }
            }
            is MessengerIntent.DismissClick -> {
                launch { MessengerEventManager.send(MessengerHostEvent.DismissRequest) }
            }
            is MessengerIntent.SendClick -> {
                // FIXME: отправка сообщения в мессенджер пока не реализована
                return
            }
            is MessengerIntent.CallClick -> {
                launch { send(MessengerEvent.LaunchDialer(stateFlow.value.activeEmployeeEntity.employeePhone)) }
            }
            is MessengerIntent.AttachClick -> reduce { it.copy(isAttachSheetVisible = true) }
            is MessengerIntent.DismissAttachSheet -> reduce { it.copy(isAttachSheetVisible = false) }
            is MessengerIntent.AttachGalleryClick -> {
                reduce { it.copy(isAttachSheetVisible = false) }
                // FIXME: выбор фото/видео из галереи пока не реализован
            }
            is MessengerIntent.AttachCartProductsClick -> {
                reduce { it.copy(isAttachSheetVisible = false) }
                // FIXME: выбор товаров из корзины/примерки пока не реализован
            }
            is MessengerIntent.AttachCatalogClick -> {
                reduce { it.copy(isAttachSheetVisible = false) }
                // FIXME: выбор товаров из каталога пока не реализован
            }
            is MessengerIntent.MicClick -> {
                // FIXME: запись голосового сообщения пока не реализована
                return
            }
        }
    }
}
