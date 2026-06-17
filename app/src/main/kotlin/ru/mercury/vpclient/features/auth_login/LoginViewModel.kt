package ru.mercury.vpclient.features.auth_login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.auth_code.navigation.CodeRoute
import ru.mercury.vpclient.features.auth_login.event.LoginEvents
import ru.mercury.vpclient.features.auth_login.intent.LoginIntent
import ru.mercury.vpclient.features.auth_login.model.LoginModel
import ru.mercury.vpclient.shared.data.error.LoginException
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.usecase.LoginUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val loginUseCase: LoginUseCase
): ClientViewModel<LoginIntent, LoginModel, LoginEvents>(LoginModel()) {

    override fun dispatch(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.LoginClick -> {
                val phoneValidationError = authenticationInteractor.validateRequiredPhone(stateFlow.value.phone)
                when {
                    phoneValidationError != null -> {
                        reduce { it.copy(phoneValidationError = phoneValidationError) }
                    }
                    else -> {
                        launch {
                            reduce { it.copy(phoneValidationError = null, isLoading = true) }
                            loginUseCase(stateFlow.value.phone).getOrThrow()
                            MainEventManager.send(CodeRoute)
                        }
                    }
                }
            }
            is LoginIntent.HideKeyboard -> launch { send(LoginEvents.ClearFocus) }
            is LoginIntent.EnterPhone -> reduce { it.copy(phone = normalizePhoneInput(intent.phone), phoneValidationError = null) }
            is LoginIntent.OnKeyboardDone -> {
                launch {
                    val canClearFocus = authenticationInteractor.validateRequiredPhone(stateFlow.value.phone) == null
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
