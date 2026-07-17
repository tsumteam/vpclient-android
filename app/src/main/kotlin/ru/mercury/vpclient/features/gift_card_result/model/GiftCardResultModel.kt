package ru.mercury.vpclient.features.gift_card_result.model

import ru.mercury.vpclient.shared.mvi.Model

data class GiftCardResultModel(
    val isPaid: Boolean = false,
    val orderNumber: String = "",
    val email: String = "",
    val phone: String = "",
    val deliveryDate: String = "",
    val deliveryTime: String = ""
): Model
