package ru.mercury.vpclient.features.checkout_bank_card_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutBankCardIntent: Intent {
    data object DismissClick: CheckoutBankCardIntent
    data object SaveCardClick: CheckoutBankCardIntent
    data object PayClick: CheckoutBankCardIntent
    data object CardNumberFocusLost: CheckoutBankCardIntent
    data object ExpirationDateFocusLost: CheckoutBankCardIntent
    data object CvvFocusLost: CheckoutBankCardIntent
    data class CardNumberChange(val value: String): CheckoutBankCardIntent
    data class ExpirationDateChange(val value: String): CheckoutBankCardIntent
    data class CvvChange(val value: String): CheckoutBankCardIntent
}
