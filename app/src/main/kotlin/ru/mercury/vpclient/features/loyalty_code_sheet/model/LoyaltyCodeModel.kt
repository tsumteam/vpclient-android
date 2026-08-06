package ru.mercury.vpclient.features.loyalty_code_sheet.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardMode
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.Companion.CODE_LENGTH
import ru.mercury.vpclient.shared.mvi.Model

data class LoyaltyCodeModel(
    val mode: LoyaltyAddCardMode,
    val phone: String = "",
    val cardNumber: String = "",
    val code: String = "",
    val isLoading: Boolean = false,
    val isResendLoading: Boolean = false,
    val isCodeErrorVisible: Boolean = false,
    val resendTimerStartedAt: Long = System.currentTimeMillis(),
    val resendSecondsLeft: Int = 0,
    val resendTimerJob: Job? = null
): Model {

    val isConfirmEnabled: Boolean
        get() = code.length == CODE_LENGTH
}
