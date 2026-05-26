package ru.mercury.vpclient.features.fitting_confirmation.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingConfirmationIntent: Intent {
    data object BackClick: FittingConfirmationIntent
    data object ConfirmClick: FittingConfirmationIntent
    data class SelectPlace(val index: Int): FittingConfirmationIntent
    data class SelectDay(val index: Int): FittingConfirmationIntent
    data class SelectInterval(val interval: String): FittingConfirmationIntent
}
