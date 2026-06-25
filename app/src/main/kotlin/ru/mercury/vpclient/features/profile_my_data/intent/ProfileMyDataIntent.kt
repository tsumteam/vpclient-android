package ru.mercury.vpclient.features.profile_my_data.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileMyDataIntent: Intent {
    data object CollectCartCount: ProfileMyDataIntent
    data object CollectFittingCount: ProfileMyDataIntent
    data object CollectActiveEmployee: ProfileMyDataIntent
    data object LoadCartData: ProfileMyDataIntent
    data object LoadCurrentUser: ProfileMyDataIntent
    data object BackClick: ProfileMyDataIntent
    data object CartClick: ProfileMyDataIntent
    data object FittingClick: ProfileMyDataIntent
    data object MessengerClick: ProfileMyDataIntent
    data object ShowDeleteProfileDialog: ProfileMyDataIntent
    data object DismissDeleteProfileDialog: ProfileMyDataIntent
    data object DeleteProfile: ProfileMyDataIntent
}
