package ru.mercury.vpclient.features.profile_delivery.model

import ru.mercury.vpclient.shared.data.PROFILE_DELIVERY_URL
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileDeliveryModel(
    val url: String = PROFILE_DELIVERY_URL
): Model
