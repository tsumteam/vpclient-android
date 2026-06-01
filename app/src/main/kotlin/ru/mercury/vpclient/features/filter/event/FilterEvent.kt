package ru.mercury.vpclient.features.filter.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface FilterEvent: Event {
    data object RefreshProducts: FilterEvent
    data class SnackbarMessage(val message: String): FilterEvent
}
