package ru.mercury.vpclient.features.brands.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface BrandsEvent: Event {
    data class SnackbarErrorMessage(val message: String): BrandsEvent
}
