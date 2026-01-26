package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.DeliveryResponse
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity

class ChildDeliveryEntityProvider: PreviewParameterProvider<DeliveryEntity> {
    override val values: Sequence<DeliveryEntity> = sequenceOf(
        DeliveryEntity.Empty.copy(
            deliveryId = "ЗД02316904",
            deliveryStatus = DeliveryResponse.DELIVERY_STATUS_IN_PROCESS,
            detailedStatus = DeliveryResponse.DETAILED_STATUS_FITTING_COMPLETED,
            codeGoal = "Примерка"
        )
    )
}
