package ru.mercury.vpclient.features.profile_loyalty_qr.model

import ru.mercury.vpclient.shared.mvi.Model

data class ProfileLoyaltyQrModel(
    val qrCode: String = ""
): Model
