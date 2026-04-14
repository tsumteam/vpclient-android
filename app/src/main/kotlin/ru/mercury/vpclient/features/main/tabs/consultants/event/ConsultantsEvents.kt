package ru.mercury.vpclient.features.main.tabs.consultants.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ConsultantsEvents: Event {
    data class SnackbarMessage(val message: String): ConsultantsEvents
}
