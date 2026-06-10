package ru.mercury.vpclient.features.profile_qr.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileQrIntent: Intent {
    data object LoadQrCode: ProfileQrIntent
    data object CloseClick: ProfileQrIntent
}
