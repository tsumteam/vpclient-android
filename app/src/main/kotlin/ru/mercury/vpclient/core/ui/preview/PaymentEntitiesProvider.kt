package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.PaymentMethodResponse
import ru.mercury.vpclient.core.network.response.PaymentSbpStatusResponse
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity

class PaymentEntitiesProvider: PreviewParameterProvider<List<PaymentEntity>> {
    override val values: Sequence<List<PaymentEntity>> = sequenceOf(
        listOf(
            PaymentEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_SBP,
                paymentName = "СБП",
                paymentType = "СБП",
                amount = 50000.0,
                status = PaymentSbpStatusResponse.STATUS_COMPLETED
            ),
            PaymentEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_CARD,
                paymentName = "Банковская карта",
                paymentType = "Банковская карта"
            ),
            PaymentEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_CASH,
                paymentName = "Наличные",
                paymentType = "Наличные"
            ),
            PaymentEntity.Empty.copy(
                paymentMethodId = PaymentMethodResponse.PAYMENT_ID_CREDIT_MAESTRO,
                paymentName = "Карта Maestro",
                paymentType = "КредРубMaestro"
            )
        )
    )
}
