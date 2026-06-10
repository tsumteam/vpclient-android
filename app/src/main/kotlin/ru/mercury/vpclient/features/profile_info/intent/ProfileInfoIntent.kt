package ru.mercury.vpclient.features.profile_info.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileInfoIntent: Intent {
    data object CollectCartSize: ProfileInfoIntent
    data object CollectActiveEmployee: ProfileInfoIntent
    data object LoadCartData: ProfileInfoIntent
    data object BackClick: ProfileInfoIntent
    data object PaymentClick: ProfileInfoIntent
    data object DeliveryClick: ProfileInfoIntent
    data object ReturnClick: ProfileInfoIntent
    data object PrivacyPolicyClick: ProfileInfoIntent
    data object GiftCardClick: ProfileInfoIntent
    data object ContactsClick: ProfileInfoIntent
    data object CartClick: ProfileInfoIntent
    data object FittingClick: ProfileInfoIntent
    data object MessengerClick: ProfileInfoIntent
}
