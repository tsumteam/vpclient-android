package ru.mercury.vpclient.features.checkout_bank_card_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutBankCardSheetIntent: Intent {
    data object DismissRequest: CheckoutBankCardSheetIntent
    data object SaveCardClick: CheckoutBankCardSheetIntent
    data object PayClick: CheckoutBankCardSheetIntent
    data object CardNumberFocusLost: CheckoutBankCardSheetIntent
    data object ExpirationDateFocusLost: CheckoutBankCardSheetIntent
    data class CardNumberChange(val value: String): CheckoutBankCardSheetIntent
    data class ExpirationDateChange(val value: String): CheckoutBankCardSheetIntent
    data class CvvChange(val value: String): CheckoutBankCardSheetIntent
}
