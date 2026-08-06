package ru.mercury.vpclient.features.checkout_bonus_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutBonusIntent: Intent {
    data object DismissRequest: CheckoutBonusIntent
    data object ConfirmClick: CheckoutBonusIntent
    data object ResendCodeClick: CheckoutBonusIntent
    data class CodeChange(val code: String): CheckoutBonusIntent
}
