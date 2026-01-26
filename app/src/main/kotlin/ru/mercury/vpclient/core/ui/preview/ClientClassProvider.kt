package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.DeliveryResponse

class ClientClassProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf(
        DeliveryResponse.CLIENT_CLASS_VIP1,
        DeliveryResponse.CLIENT_CLASS_VIP2,
        DeliveryResponse.CLIENT_CLASS_VIP3,
        DeliveryResponse.CLIENT_CLASS_VIP4,
        DeliveryResponse.CLIENT_CLASS_VIP35,
        DeliveryResponse.CLIENT_CLASS_VIPIN,
        DeliveryResponse.CLIENT_CLASS_PHOTO,
        DeliveryResponse.CLIENT_CLASS_NEW
    )
}
