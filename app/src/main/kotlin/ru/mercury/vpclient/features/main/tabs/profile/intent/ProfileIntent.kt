package ru.mercury.vpclient.features.main.tabs.profile.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileIntent: Intent {
    data object CollectCartSize: ProfileIntent
    data object CollectActiveEmployee: ProfileIntent
    data object LoadEmployees: ProfileIntent
    data object LoadCartData: ProfileIntent
    data object Logout: ProfileIntent
    data object CartClick: ProfileIntent
    data object FittingClick: ProfileIntent
    data object MessengerClick: ProfileIntent
}
