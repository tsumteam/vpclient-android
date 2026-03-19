package ru.mercury.vpclient.features.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.exception.LoginException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.ktx.normalizePhoneInput
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.features.code.navigation.CodeRoute
import ru.mercury.vpclient.features.login.event.LoginEvents
import ru.mercury.vpclient.features.login.intent.LoginIntent
import ru.mercury.vpclient.features.login.model.LoginModel
import ru.mercury.vpclient.activity.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<LoginIntent, LoginModel, LoginEvents>(LoginModel()) {

    override fun dispatch(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.LoginClick -> {
                val phoneValidationError = interactor.validateRequiredPhone(stateFlow.value.phone)
                when {
                    phoneValidationError != null -> reduce { it.copy(phoneValidationError = phoneValidationError) }
                    else -> {
                        launch {
                            reduce { it.copy(phoneValidationError = null, isLoading = true) }
                            interactor.login(stateFlow.value.phone)
                            MainEventManager.send(CodeRoute)
                        }
                    }
                }
            }
            is LoginIntent.EnterPhone -> reduce { it.copy(phone = normalizePhoneInput(intent.phone), phoneValidationError = null) }
            is LoginIntent.OnKeyboardDone -> {
                launch {
                    val canClearFocus = interactor.validateRequiredPhone(stateFlow.value.phone) == null
                    dispatch(LoginIntent.LoginClick)
                    if (canClearFocus) {
                        send(LoginEvents.ClearFocus)
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is LoginException -> {
                launch {
                    reduce { it.copy(isLoading = false) }
                    send(LoginEvents.SnackbarMessage(throwable.message))
                }
            }
            else -> super.catch(throwable)
        }
    }
}
