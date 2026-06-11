package ru.mercury.vpclient.features.profile.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileEvent: Event {
    data class SnackbarMessage(val message: String): ProfileEvent
}
