package ru.mercury.vpclient.features.search.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface SearchEvent: Event {
    data class SnackbarErrorMessage(val message: String): SearchEvent
}
