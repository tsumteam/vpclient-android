package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.PaymentMethodResponse
import ru.mercury.vpclient.core.persistence.database.entity.PaymentMethodEntity

class PaymentMethodEntitiesProvider: PreviewParameterProvider<List<PaymentMethodEntity>> {
    override val values: Sequence<List<PaymentMethodEntity>> = sequenceOf(
        listOf(
            PaymentMethodEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_CARD,
                paymentName = "Банковская карта"
            ),
            PaymentMethodEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_SBP,
                paymentName = "СБП"
            ),
            PaymentMethodEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_CASH,
                paymentName = "Наличные"
            ),
            PaymentMethodEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_CREDIT_MAESTRO,
                paymentName = "Карта Maestro"
            )
        )
    )
}
