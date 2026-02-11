package ru.mercury.vpclient.features.code.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.core.entity.CodeValidationError
import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class CodeModel(
    val code: String = "",
    val clientEntity: ClientEntity = ClientEntity.Empty,
    val codeValidationError: CodeValidationError? = null,
    val resendSecondsLeft: Int = 0,
    val resendTimerJob: Job? = null,
    val resendCodeJob: Job? = null,
    val isLoading: Boolean = false
): Model {

    val isResendLoading: Boolean
        get() = resendCodeJob != null && resendCodeJob.isActive == true
}
