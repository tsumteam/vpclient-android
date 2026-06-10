package ru.mercury.vpclient.features.profile_qr.model

import ru.mercury.vpclient.shared.mvi.Model

data class ProfileQrModel(
    val qrCode: String = ""
): Model
