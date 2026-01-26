package ru.mercury.vpclient.core.event

sealed interface FocusEvent {
    data object Clear: FocusEvent
    data object Down: FocusEvent
}
