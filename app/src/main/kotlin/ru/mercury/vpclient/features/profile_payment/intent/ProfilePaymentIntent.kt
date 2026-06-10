package ru.mercury.vpclient.features.profile_payment.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfilePaymentIntent: Intent {
    data object BackClick: ProfilePaymentIntent
}
