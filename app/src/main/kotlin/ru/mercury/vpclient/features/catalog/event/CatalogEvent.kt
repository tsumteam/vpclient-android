package ru.mercury.vpclient.features.catalog.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CatalogEvent: Event {
    data class SnackbarMessage(val message: String): CatalogEvent
}
