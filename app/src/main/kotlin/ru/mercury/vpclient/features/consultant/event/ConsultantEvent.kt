package ru.mercury.vpclient.features.consultant.event

import ru.mercury.vpclient.core.mvi.Event

sealed interface ConsultantEvent: Event {
    data class SnackbarMessage(val message: String): ConsultantEvent
}
