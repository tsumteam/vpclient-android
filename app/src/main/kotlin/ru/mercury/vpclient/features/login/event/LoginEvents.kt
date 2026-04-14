package ru.mercury.vpclient.features.login.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface LoginEvents: Event {
    data object ClearFocus: LoginEvents
    data class SnackbarMessage(val message: String): LoginEvents
}
