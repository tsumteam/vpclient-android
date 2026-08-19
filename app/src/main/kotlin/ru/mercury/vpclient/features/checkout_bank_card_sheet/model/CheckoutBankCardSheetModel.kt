package ru.mercury.vpclient.features.checkout_bank_card_sheet.model

import ru.mercury.vpclient.shared.domain.mapper.isValidBankCardExpirationDate
import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutBankCardSheetModel(
    val cardNumber: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val isSaveCardChecked: Boolean = false,
    val isCardNumberErrorVisible: Boolean = false,
    val isExpirationDateErrorVisible: Boolean = false,
    val isCvvErrorVisible: Boolean = false,
    val isLoading: Boolean = false
): Model {

    val paymentSystem: CheckoutBankCardPaymentSystem
        get() {
            val digits = cardNumber.filter(Char::isDigit)
            return when {
                digits.startsWith("4") -> CheckoutBankCardPaymentSystem.Visa
                digits.take(4).toIntOrNull() in 2200..2204 -> CheckoutBankCardPaymentSystem.Mir
                digits.take(2).toIntOrNull() in 51..55 -> CheckoutBankCardPaymentSystem.Mastercard
                else -> CheckoutBankCardPaymentSystem.Unknown
            }
        }

    val isPayEnabled: Boolean
        get() = cardNumber.count(Char::isDigit) >= MIN_CARD_NUMBER_LENGTH &&
            expirationDate.isValidBankCardExpirationDate &&
            cvv.length == CVV_LENGTH &&
            !isLoading

    val isCardNumberErrorIconVisible: Boolean
        get() = isCardNumberErrorVisible

    val isExpirationDateErrorIconVisible: Boolean
        get() = isExpirationDateErrorVisible

    val isCvvErrorIconVisible: Boolean
        get() = isCvvErrorVisible

    val isLoadingIndicatorVisible: Boolean
        get() = isLoading

    companion object {
        const val MIN_CARD_NUMBER_LENGTH = 12
        const val MAX_CARD_NUMBER_LENGTH = 19
        const val CVV_LENGTH = 3
    }
}
