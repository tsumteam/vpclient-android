package ru.mercury.vpclient.features.banner.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface BannerIntent: Intent {
    data object CollectActiveEmployee: BannerIntent
    data object LoadCartData: BannerIntent
    data object BackClick: BannerIntent
    data object SearchClick: BannerIntent
    data object FittingClick: BannerIntent
    data object CartClick: BannerIntent
    data object MessengerClick: BannerIntent
}
