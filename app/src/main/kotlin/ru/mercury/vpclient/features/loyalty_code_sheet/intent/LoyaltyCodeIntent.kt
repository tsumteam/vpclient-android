package ru.mercury.vpclient.features.loyalty_code_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface LoyaltyCodeIntent: Intent {
    data object DismissRequest: LoyaltyCodeIntent
    data object StartResendTimerTicker: LoyaltyCodeIntent
    data class CodeChange(val code: String): LoyaltyCodeIntent
    data object ConfirmClick: LoyaltyCodeIntent
    data object ResendCodeClick: LoyaltyCodeIntent
}
