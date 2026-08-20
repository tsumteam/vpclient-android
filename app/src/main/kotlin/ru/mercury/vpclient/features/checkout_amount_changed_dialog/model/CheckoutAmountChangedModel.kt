package ru.mercury.vpclient.features.checkout_amount_changed_dialog.model

import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutAmountChangedModel(
    val message: String
): Model
