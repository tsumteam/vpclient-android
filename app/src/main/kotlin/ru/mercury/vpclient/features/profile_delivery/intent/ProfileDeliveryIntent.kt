package ru.mercury.vpclient.features.profile_delivery.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileDeliveryIntent: Intent {
    data object BackClick: ProfileDeliveryIntent
}
