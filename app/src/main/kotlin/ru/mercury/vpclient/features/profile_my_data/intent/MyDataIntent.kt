package ru.mercury.vpclient.features.profile_my_data.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MyDataIntent: Intent {
    data object CollectCartSize: MyDataIntent
    data object CollectActiveEmployee: MyDataIntent
    data object LoadCartData: MyDataIntent
    data object LoadCurrentUser: MyDataIntent
    data object BackClick: MyDataIntent
    data object CartClick: MyDataIntent
    data object FittingClick: MyDataIntent
    data object MessengerClick: MyDataIntent
    data object ShowDeleteProfileDialog: MyDataIntent
    data object DismissDeleteProfileDialog: MyDataIntent
    data object DeleteProfile: MyDataIntent
}
