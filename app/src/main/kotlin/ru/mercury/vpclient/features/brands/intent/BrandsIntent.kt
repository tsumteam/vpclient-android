package ru.mercury.vpclient.features.brands.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface BrandsIntent: Intent {
    data object CollectCartSize: BrandsIntent
    data object CollectActiveEmployee: BrandsIntent
    data object LoadCartData: BrandsIntent
    data object SearchClick: BrandsIntent
    data object CartClick: BrandsIntent
    data object FittingClick: BrandsIntent
    data object MessengerClick: BrandsIntent
}
