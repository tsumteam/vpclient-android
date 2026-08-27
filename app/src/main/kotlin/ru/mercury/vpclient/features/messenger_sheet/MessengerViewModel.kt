package ru.mercury.vpclient.features.messenger_sheet

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEvent
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEventManager
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
                launch { MessengerEventManager.send(MessengerEvent.DismissRequest) }
            }
            is MessengerIntent.SendClick -> {
                // FIXME: отправка сообщения в мессенджер пока не реализована
                return
            }
            is MessengerIntent.CallClick -> {
                // FIXME: сформировать и запустить Intent(Intent.ACTION_DIAL, "tel:" + activeEmployeeEntity.employeePhone) для звонка консультанту
                return
            }
            is MessengerIntent.AttachClick -> {
                // FIXME: выбор вложения пока не реализован
                return
            }
            is MessengerIntent.MicClick -> {
                // FIXME: запись голосового сообщения пока не реализована
                return
            }
        }
    }
}
