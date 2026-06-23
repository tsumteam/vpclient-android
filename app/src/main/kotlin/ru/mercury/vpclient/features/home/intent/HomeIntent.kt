package ru.mercury.vpclient.features.home.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface HomeIntent: Intent {
    data object CollectCartSize: HomeIntent
    data object CollectActiveEmployee: HomeIntent
    data object LoadCartData: HomeIntent
    data object SearchClick: HomeIntent
    data object CartClick: HomeIntent
    data object FittingClick: HomeIntent
    data object MessengerClick: HomeIntent
}
