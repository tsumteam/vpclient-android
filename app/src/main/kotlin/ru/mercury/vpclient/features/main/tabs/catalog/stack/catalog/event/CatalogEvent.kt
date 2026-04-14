package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CatalogEvent: Event {
    data class SnackbarMessage(val message: String): CatalogEvent
}
