package ru.mercury.vpclient.features.main.tabs.home.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface HomeIntent: Intent {
    data object CollectCartSize: HomeIntent
    data object CollectActiveEmployee: HomeIntent
    data object LoadCartData: HomeIntent
    data object CartClick: HomeIntent
}
