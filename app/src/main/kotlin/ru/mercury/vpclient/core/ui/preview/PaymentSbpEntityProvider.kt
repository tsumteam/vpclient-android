package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.PaymentMethodResponse
import ru.mercury.vpclient.core.network.response.PaymentSbpStatusResponse
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity

class PaymentSbpEntityProvider: PreviewParameterProvider<PaymentEntity> {
    override val values: Sequence<PaymentEntity> = sequenceOf(
        PaymentEntity.Empty.copy(
            paymentMethodId = PaymentMethodResponse.PAYMENT_ID_SBP,
            paymentName = "СБП",
            paymentType = "СБП",
            amount = 400000.0,
            status = PaymentSbpStatusResponse.STATUS_INPROGRESS
        )
    )
}
