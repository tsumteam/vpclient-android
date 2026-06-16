package ru.mercury.vpclient.features.profile_loyalty_qr.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyQrIntent: Intent {
    data object LoadQrCode: ProfileLoyaltyQrIntent
    data object CloseClick: ProfileLoyaltyQrIntent
}
