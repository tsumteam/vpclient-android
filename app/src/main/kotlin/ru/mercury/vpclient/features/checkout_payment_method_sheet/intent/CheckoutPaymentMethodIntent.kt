package ru.mercury.vpclient.features.checkout_payment_method_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutPaymentMethodIntent: Intent {
    data object DismissClick: CheckoutPaymentMethodIntent
    data object AddNewCardClick: CheckoutPaymentMethodIntent
    data object PayClick: CheckoutPaymentMethodIntent
    data class CardClick(val cardId: String): CheckoutPaymentMethodIntent
    data class DeleteCardClick(val cardId: String): CheckoutPaymentMethodIntent
}
