package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.ui.components.fitting.FittingDeliveryHeaderState

data class CartFittingDeliveryGroup(
    val id: String,
    val fittingType: FittingType,
    val header: FittingDeliveryHeaderState,
    val productGroups: List<CartProductGroup>
)
