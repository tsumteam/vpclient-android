package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.event

import ru.mercury.vpclient.core.mvi.Event

sealed interface CatalogEvent: Event {
    data class SnackbarMessage(val message: String): CatalogEvent
}
