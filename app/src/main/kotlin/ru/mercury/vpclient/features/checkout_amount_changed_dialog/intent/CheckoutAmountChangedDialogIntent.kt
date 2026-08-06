package ru.mercury.vpclient.features.checkout_amount_changed_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutAmountChangedDialogIntent: Intent {
    data object ContinueClick: CheckoutAmountChangedDialogIntent
}
