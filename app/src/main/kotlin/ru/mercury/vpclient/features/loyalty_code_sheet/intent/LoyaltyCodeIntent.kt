package ru.mercury.vpclient.features.loyalty_code_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface LoyaltyCodeIntent: Intent {
    data object DismissClick: LoyaltyCodeIntent
    data object StartResendTimerTicker: LoyaltyCodeIntent
    data object ConfirmClick: LoyaltyCodeIntent
    data object ResendCodeClick: LoyaltyCodeIntent
    data class CodeChange(val code: String): LoyaltyCodeIntent
}
