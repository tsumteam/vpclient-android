package ru.mercury.vpclient.features.consultants.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ConsultantsEvents: Event {
    data class SnackbarMessage(val message: String): ConsultantsEvents
    data class LaunchDialer(val phone: String): ConsultantsEvents
}
