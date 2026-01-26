package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.PaymentSbpStatusResponse
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity

class PaymentEntityProvider: PreviewParameterProvider<PaymentEntity> {
    override val values: Sequence<PaymentEntity> = sequenceOf(
        PaymentEntity.Empty.copy(
            paymentName = "СБП",
            status = PaymentSbpStatusResponse.STATUS_INPROGRESS
        ),
        PaymentEntity.Empty.copy(
            paymentName = "СБП",
            status = PaymentSbpStatusResponse.STATUS_COMPLETED
        ),
        PaymentEntity.Empty.copy(
            paymentName = "Банковская карта",
            status = PaymentSbpStatusResponse.STATUS_INPROGRESS
        ),
        PaymentEntity.Empty.copy(
            paymentName = "Банковская карта",
            status = PaymentSbpStatusResponse.STATUS_COMPLETED
        ),
        PaymentEntity.Empty.copy(
            paymentName = "Наличные",
            status = PaymentSbpStatusResponse.STATUS_INPROGRESS
        ),
        PaymentEntity.Empty.copy(
            paymentName = "Наличные",
            status = PaymentSbpStatusResponse.STATUS_COMPLETED
        ),
        PaymentEntity.Empty.copy(
            paymentName = "Карта Maestro",
            status = PaymentSbpStatusResponse.STATUS_INPROGRESS
        ),
        PaymentEntity.Empty.copy(
            paymentName = "Карта Maestro",
            status = PaymentSbpStatusResponse.STATUS_COMPLETED
        )
    )
}
