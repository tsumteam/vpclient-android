package ru.mercury.vpclient.features.register

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.data.error.RegisterException
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.features.code.navigation.CodeRoute
import ru.mercury.vpclient.features.register.event.RegisterEvents
import ru.mercury.vpclient.features.register.intent.RegisterIntent
import ru.mercury.vpclient.features.register.model.RegisterModel
import ru.mercury.vpclient.activity.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<RegisterIntent, RegisterModel, RegisterEvents>(RegisterModel()) {

    override fun dispatch(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.RegisterClick -> {
                val nameValidationError = interactor.validateRequiredName(stateFlow.value.name)
                val phoneValidationError = interactor.validateRequiredPhone(stateFlow.value.phone)
                when {
                    nameValidationError != null || phoneValidationError != null -> reduce { it.copy(nameValidationError = nameValidationError, phoneValidationError = phoneValidationError) }
                    else -> {
                        launch {
                            reduce { it.copy(nameValidationError = null, phoneValidationError = null, isLoading = true) }
                            interactor.register(stateFlow.value.phone, stateFlow.value.name)
                            reduce { it.copy(isLoading = false) }
                            MainEventManager.send(CodeRoute)
                        }
                    }
                }
            }
            is RegisterIntent.MoveFocusDown -> launch { send(RegisterEvents.MoveFocusDown) }
            is RegisterIntent.EnterName -> reduce { it.copy(name = intent.name, nameValidationError = null) }
            is RegisterIntent.EnterPhone -> reduce { it.copy(phone = normalizePhoneInput(intent.phone), phoneValidationError = null) }
            is RegisterIntent.OnKeyboardDone -> {
                launch {
                    val canClearFocus = interactor.validateRequiredName(stateFlow.value.name) == null && interactor.validateRequiredPhone(stateFlow.value.phone) == null
                    dispatch(RegisterIntent.RegisterClick)
                    if (canClearFocus) {
                        send(RegisterEvents.ClearFocus)
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RegisterException -> {
                launch {
                    reduce { it.copy(isLoading = false) }
                    send(RegisterEvents.SnackbarMessage(throwable.message))
                }
            }
            else -> super.catch(throwable)
        }
    }
}
