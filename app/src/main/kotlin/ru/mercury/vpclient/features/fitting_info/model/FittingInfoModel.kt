package ru.mercury.vpclient.features.fitting_info.model

import ru.mercury.vpclient.shared.mvi.Model

data class FittingInfoModel(
    val address: String = "",
    val deliveryDate: String = ""
): Model
