package ru.mercury.vpclient.features.register.event

import ru.mercury.vpclient.core.mvi.Event

sealed interface RegisterEvents: Event {
    data object MoveFocusDown: RegisterEvents
    data object ClearFocus: RegisterEvents
    data object OpenUri: RegisterEvents
    data class SnackbarMessage(val message: String): RegisterEvents
}
