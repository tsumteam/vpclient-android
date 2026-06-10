package ru.mercury.vpclient.features.profile_contacts.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileContactsEvent: Event {
    data class OpenDialer(val phone: String): ProfileContactsEvent
    data class OpenEmail(val email: String): ProfileContactsEvent
}
