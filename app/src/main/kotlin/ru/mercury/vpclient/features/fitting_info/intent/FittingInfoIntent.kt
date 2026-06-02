package ru.mercury.vpclient.features.fitting_info.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingInfoIntent: Intent {
    data object Init: FittingInfoIntent
    data object BackClick: FittingInfoIntent
}
