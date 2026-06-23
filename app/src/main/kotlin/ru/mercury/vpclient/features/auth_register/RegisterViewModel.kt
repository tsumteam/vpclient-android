package ru.mercury.vpclient.features.auth_register

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.auth_code.navigation.CodeRoute
import ru.mercury.vpclient.features.auth_register.event.RegisterEvents
import ru.mercury.vpclient.features.auth_register.intent.RegisterIntent
import ru.mercury.vpclient.features.auth_register.model.RegisterModel
import ru.mercury.vpclient.shared.data.error.RegisterException
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateNameUseCase
import ru.mercury.vpclient.shared.domain.usecase.AuthValidatePhoneUseCase
import ru.mercury.vpclient.shared.domain.usecase.RegisterUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authValidateNameUseCase: AuthValidateNameUseCase,
    private val authValidatePhoneUseCase: AuthValidatePhoneUseCase,
    private val registerUseCase: RegisterUseCase
): ClientViewModel<RegisterIntent, RegisterModel, RegisterEvents>(RegisterModel()) {

    override fun dispatch(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.RegisterClick -> {
                launch {
                    val nameValidationError = authValidateNameUseCase(stateFlow.value.name).getOrThrow()
                    val phoneValidationError = authValidatePhoneUseCase(stateFlow.value.phone).getOrThrow()
                    when {
                        nameValidationError != null || phoneValidationError != null -> {
                            reduce {
                                it.copy(
                                    nameValidationError = nameValidationError,
                                    phoneValidationError = phoneValidationError
                                )
                            }
                        }
                        else -> {
                            reduce {
                                it.copy(
                                    nameValidationError = null,
                                    phoneValidationError = null,
                                    isLoading = true
                                )
                            }
                            val params = RegisterUseCase.Params(
                                phone = stateFlow.value.phone,
                                name = stateFlow.value.name
                            )
                            registerUseCase(params).getOrThrow()
                            reduce { it.copy(isLoading = false) }
                            MainEventManager.send(CodeRoute)
                        }
                    }
                }
            }
            is RegisterIntent.HideKeyboard -> launch { send(RegisterEvents.ClearFocus) }
            is RegisterIntent.MoveFocusDown -> launch { send(RegisterEvents.MoveFocusDown) }
            is RegisterIntent.EnterName -> {
                reduce { it.copy(name = intent.name, nameValidationError = null) }
            }
            is RegisterIntent.EnterPhone -> {
                reduce { it.copy(
                    phone = normalizePhoneInput(intent.phone),
                    phoneValidationError = null
                ) }
            }
            is RegisterIntent.OnKeyboardDone -> {
                launch {
                    val canClearFocus = authValidateNameUseCase(stateFlow.value.name).getOrThrow() == null &&
                        authValidatePhoneUseCase(stateFlow.value.phone).getOrThrow() == null
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
