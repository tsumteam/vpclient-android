package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.DeliveryResponse
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity

class DeliveryEntityProvider: PreviewParameterProvider<DeliveryEntity> {
    override val values: Sequence<DeliveryEntity> = sequenceOf(
        DeliveryEntity.Empty.copy(
            deliveryId = "ЗД02316904",
            sourceDocId = "VPH079227",
            sellerName = "Киореску Виктория",
            sellerPhone = "+7 777 777 77 77",
            clientName = "Пиотровски Рената",
            clientPhone = "+7 777 777 77 77",
            clientCanCall = true,
            clientClass = DeliveryResponse.CLIENT_CLASS_VIP2
        )
    )
}
