package ru.mercury.vpclient.features.auth_code

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.auth_code.event.CodeEvents
import ru.mercury.vpclient.features.auth_code.intent.CodeIntent
import ru.mercury.vpclient.features.auth_code.model.CodeModel
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.data.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.shared.domain.mapper.resendCodeTimerSec
import ru.mercury.vpclient.shared.domain.usecase.AuthContinueLoginUseCase
import ru.mercury.vpclient.shared.domain.usecase.AuthContinueLoginUseCase.AuthContinueLoginException
import ru.mercury.vpclient.shared.domain.usecase.AuthResendCodeUseCase
import ru.mercury.vpclient.shared.domain.usecase.AuthResendCodeUseCase.AuthResendCodeException
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.CodeValidationError
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.Companion.CODE_LENGTH
import ru.mercury.vpclient.shared.domain.usecase.ClientEntityFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CodeViewModel @Inject constructor(
    private val authValidateCodeUseCase: AuthValidateCodeUseCase,
    private val authContinueLoginUseCase: AuthContinueLoginUseCase,
    private val authResendCodeUseCase: AuthResendCodeUseCase,
    private val clientEntityFlowUseCase: ClientEntityFlowUseCase
): ClientViewModel<CodeIntent, CodeModel, CodeEvents>(CodeModel()) {

    init {
        dispatch(CodeIntent.CollectClientEntity)
        dispatch(CodeIntent.StartResendTimerTicker)
    }

    override fun dispatch(intent: CodeIntent) {
        when (intent) {
            is CodeIntent.CollectClientEntity -> {
                launch {
                    clientEntityFlowUseCase(Unit).collectLatest { entity ->
                        reduce { it.copy(
                            clientEntity = entity,
                            resendSecondsLeft = entity.resendCodeTimerSec()
                        ) }
                    }
                }
            }
            is CodeIntent.StartResendTimerTicker -> {
                stateFlow.value.resendTimerJob?.cancel()
                val job = launch {
                    while (true) {
                        delay(CODE_RESEND_TIMER_DELAY.milliseconds)
                        reduce { state ->
                            val resendSecondsLeft = state.clientEntity.resendCodeTimerSec()
                            when {
                                resendSecondsLeft == state.resendSecondsLeft -> state
                                else -> state.copy(resendSecondsLeft = resendSecondsLeft)
                            }
                        }
                    }
                }
                reduce { it.copy(resendTimerJob = job) }
            }
            is CodeIntent.ConfirmClick -> {
                launch {
                    val codeValidationError = authValidateCodeUseCase(stateFlow.value.code).getOrThrow()
                    when {
                        codeValidationError == CodeValidationError.Empty -> {
                            reduce { it.copy(codeValidationError = codeValidationError) }
                        }
                        else -> {
                            reduce { it.copy(codeValidationError = null, isLoading = true) }
                            try {
                                authContinueLoginUseCase(stateFlow.value.code).getOrThrow()
                                MainEventManager.send(MainRoute())
                            } finally {
                                reduce { it.copy(isLoading = false) }
                            }
                        }
                    }
                }
            }
            is CodeIntent.HideKeyboard -> launch { send(CodeEvents.ClearFocus) }
            is CodeIntent.ResendCodeClick -> {
                val state = stateFlow.value
                if (state.resendSecondsLeft > 0 || state.isResendLoading) return
                val job = launch {
                    try {
                        authResendCodeUseCase(Unit).getOrThrow()
                    } finally {
                        reduce { it.copy(resendCodeJob = null) }
                    }
                }
                reduce { it.copy(resendCodeJob = job) }
            }
            is CodeIntent.EnterCode -> {
                reduce { it.copy(
                    code = intent.code.filter(Char::isDigit).take(CODE_LENGTH),
                    codeValidationError = null
                ) }
            }
            is CodeIntent.OnKeyboardDone -> {
                launch {
                    val canClearFocus = authValidateCodeUseCase(stateFlow.value.code).getOrThrow() == null
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
            is AuthContinueLoginException -> {
                launch {
                    reduce { it.copy(isLoading = false) }
                    send(CodeEvents.SnackbarMessage(throwable.message))
                }
            }
            is AuthResendCodeException -> {
                launch {
                    reduce { it.copy(resendCodeJob = null) }
                    send(CodeEvents.SnackbarMessage(throwable.message))
                }
            }
            else -> super.catch(throwable)
        }
    }
}
