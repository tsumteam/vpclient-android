package ru.mercury.vpclient.features.fitting_success.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingSuccessIntent: Intent {
    data object CatalogClick: FittingSuccessIntent
    data object FittingClick: FittingSuccessIntent
}
