package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum
import ru.mercury.vpclient.shared.ui.components.fitting.FittingDeliveryHeaderState

data class FittingDeliveryData(
    val id: String = "",
    val fittingType: FittingTypeDtoEnum = FittingTypeDtoEnum.IN_THE_STORE,
    val header: FittingDeliveryHeaderState = FittingDeliveryHeaderState.Empty,
    val products: List<CartProduct> = emptyList()
)
