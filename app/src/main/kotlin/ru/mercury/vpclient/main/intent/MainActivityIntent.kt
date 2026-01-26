package ru.mercury.vpclient.main.intent

import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.PaymentMethodId
import ru.mercury.vpclient.core.mvi.Intent

sealed interface MainActivityIntent: Intent {
    data object ResolveNavigation: MainActivityIntent
    data class CenterLoading(val enabled: Boolean): MainActivityIntent
    data class PaymentCancel(val deliveryId: DeliveryId, val paymentMethodId: PaymentMethodId): MainActivityIntent
}
