package ru.mercury.vpclient.features.checkout_payment_result.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutPaymentResultIntent: Intent {
    data object PurchasesClick: CheckoutPaymentResultIntent
    data object CatalogClick: CheckoutPaymentResultIntent
}
