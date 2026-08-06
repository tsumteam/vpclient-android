package ru.mercury.vpclient.features.checkout_payment_method_sheet.model

import ru.mercury.vpclient.shared.data.entity.FittingCheckoutPaymentCardModel
import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutPaymentMethodModel(
    val cards: List<FittingCheckoutPaymentCardModel> = emptyList(),
    val isLoading: Boolean = false
): Model {

    val isPayEnabled: Boolean
        get() = cards.any { card -> card.isSelected } && !isLoading
}
