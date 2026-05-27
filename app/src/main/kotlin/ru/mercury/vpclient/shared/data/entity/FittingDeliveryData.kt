package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum

data class FittingDeliveryData(
    val id: String = "",
    val fittingType: FittingTypeDtoEnum = FittingTypeDtoEnum.IN_THE_STORE,
    val header: FittingDeliveryHeader = FittingDeliveryHeader.Empty,
    val products: List<CartProduct> = emptyList()
)
