package ru.mercury.vpclient.features.checkout_bonus_sheet.model

import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.Companion.CODE_LENGTH
import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutBonusModel(
    val code: String = "",
    val isLoading: Boolean = false,
    val isResendLoading: Boolean = false,
    val isCodeErrorTextVisible: Boolean = false,
    val resendSecondsLeft: Int = 0
): Model {

    val isConfirmEnabled: Boolean
        get() = code.length == CODE_LENGTH && !isLoading

    val isResendTextVisible: Boolean
        get() = resendSecondsLeft > 0

    val isResendLoadingIndicatorVisible: Boolean
        get() = isResendLoading

    val isLoadingIndicatorVisible: Boolean
        get() = isLoading
}
