package ru.mercury.vpclient.features.auth_code.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.CodeValidationError
import ru.mercury.vpclient.shared.mvi.Model

data class CodeModel(
    val code: String = "",
    val clientEntity: ClientEntity = ClientEntity.Empty,
    val codeValidationError: CodeValidationError? = null,
    val resendSecondsLeft: Int = 0,
    val resendTimerJob: Job? = null,
    val resendCodeJob: Job? = null,
    val confirmJob: Job? = null,
    val isLoading: Boolean = false
): Model {

    val isResendLoading: Boolean
        get() = resendCodeJob != null && resendCodeJob.isActive == true

    val isConfirmEnabled: Boolean
        get() = !isLoading

    val isCodeValidationErrorTextVisible: Boolean
        get() = codeValidationError != null

    val isResendTextVisible: Boolean
        get() = resendSecondsLeft > 0

    val isResendLoadingIndicatorVisible: Boolean
        get() = isResendLoading

    val isLoadingIndicatorVisible: Boolean
        get() = isLoading
}
