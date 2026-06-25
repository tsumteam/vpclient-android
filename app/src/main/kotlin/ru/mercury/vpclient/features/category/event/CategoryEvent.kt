package ru.mercury.vpclient.features.category.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CategoryEvent: Event {
    data class SnackbarErrorMessage(val message: String): CategoryEvent
}
