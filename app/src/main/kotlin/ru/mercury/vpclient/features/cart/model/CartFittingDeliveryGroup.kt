package ru.mercury.vpclient.features.cart.model

import ru.mercury.vpclient.shared.data.entity.FittingDeliveryHeader
import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum

data class CartFittingDeliveryGroup(
    val id: String,
    val fittingType: FittingTypeDtoEnum,
    val header: FittingDeliveryHeader,
    val productGroups: List<CartProductGroup>
)
