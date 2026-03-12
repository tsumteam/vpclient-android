package ru.mercury.vpclient.features.code

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.CODE_LENGTH
import ru.mercury.vpclient.core.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.core.entity.CodeValidationError
import ru.mercury.vpclient.core.exception.ContinueLoginException
import ru.mercury.vpclient.core.exception.LoginException
import ru.mercury.vpclient.core.exception.RegisterException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.ktx.resendCodeTimerSec
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.features.code.event.CodeEvents
import ru.mercury.vpclient.features.code.intent.CodeIntent
import ru.mercury.vpclient.features.code.model.CodeModel
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class CodeViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<CodeIntent, CodeModel, CodeEvents>(CodeModel()) {

    init {
        dispatch(CodeIntent.CollectClientEntity)
        dispatch(CodeIntent.StartResendTimerTicker)
    }

    override fun dispatch(intent: CodeIntent) {
        when (intent) {
            is CodeIntent.CollectClientEntity -> {
                launch {
                    interactor.clientEntityFlow.collectLatest { entity ->
                        reduce { it.copy(clientEntity = entity, resendSecondsLeft = entity.resendCodeTimerSec()) }
                    }
                }
            }
            is CodeIntent.StartResendTimerTicker -> {
                stateFlow.value.resendTimerJob?.cancel()
                val job = launch {
                    while (true) {
                        delay(CODE_RESEND_TIMER_DELAY)
                        reduce { state ->
                            val resendSecondsLeft = state.clientEntity.resendCodeTimerSec()
                            if (resendSecondsLeft == state.resendSecondsLeft) {
                                state
                            } else {
                                state.copy(resendSecondsLeft = resendSecondsLeft)
                            }
                        }
                    }
                }
                reduce { it.copy(resendTimerJob = job) }
            }
            is CodeIntent.ConfirmClick -> {
                val codeValidationError = interactor.validateRequiredCode(stateFlow.value.code)
                when {
                    codeValidationError == CodeValidationError.Empty -> reduce { it.copy(codeValidationError = codeValidationError) }
                    else -> {
                        launch {
                            reduce { it.copy(codeValidationError = null, isLoading = true) }
                            try {
                                interactor.continueLogin(stateFlow.value.code)
                                MainEventManager.send(MainRoute())
                            } finally {
                                reduce { it.copy(isLoading = false) }
                            }
                        }
                    }
                }
            }
            is CodeIntent.ResendCodeClick -> {
                val state = stateFlow.value
                if (state.resendSecondsLeft > 0 || state.isResendLoading) return
                val job = launch {
                    try {
                        interactor.resendCode()
                    } finally {
                        reduce { it.copy(resendCodeJob = null) }
                    }
                }
                reduce { it.copy(resendCodeJob = job) }
            }
            is CodeIntent.EnterCode -> reduce { it.copy(code = intent.code.filter(Char::isDigit).take(CODE_LENGTH), codeValidationError = null) }
            is CodeIntent.OnKeyboardDone -> {
                launch {
                    val canClearFocus = stateFlow.value.code.length == CODE_LENGTH
                    dispatch(CodeIntent.ConfirmClick)
                    if (canClearFocus) {
                        send(CodeEvents.ClearFocus)
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ContinueLoginException -> {
                launch {
                    reduce { it.copy(isLoading = false) }
                    send(CodeEvents.SnackbarMessage(throwable.message))
                }
            }
            is LoginException -> {
                launch {
                    reduce { it.copy(resendCodeJob = null) }
                    send(CodeEvents.SnackbarMessage(throwable.message))
                }
            }
            is RegisterException -> {
                launch {
                    reduce { it.copy(resendCodeJob = null) }
                    send(CodeEvents.SnackbarMessage(throwable.message))
                }
            }
            else -> super.catch(throwable)
        }
    }
}
