package ru.mercury.vpclient.features.profile_loyalty_code_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyCodeIntent: Intent {
    data object DismissRequest: ProfileLoyaltyCodeIntent
    data object StartResendTimerTicker: ProfileLoyaltyCodeIntent
    data class CodeChange(val code: String): ProfileLoyaltyCodeIntent
    data object ConfirmClick: ProfileLoyaltyCodeIntent
    data object ResendCodeClick: ProfileLoyaltyCodeIntent
}
