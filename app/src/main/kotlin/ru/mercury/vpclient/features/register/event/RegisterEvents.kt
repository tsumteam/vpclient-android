package ru.mercury.vpclient.features.register.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface RegisterEvents: Event {
    data object MoveFocusDown: RegisterEvents
    data object ClearFocus: RegisterEvents
    data class SnackbarMessage(val message: String): RegisterEvents
}
