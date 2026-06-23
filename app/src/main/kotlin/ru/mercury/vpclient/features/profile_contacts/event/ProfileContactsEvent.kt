package ru.mercury.vpclient.features.profile_contacts.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileContactsEvent: Event {
    data class LaunchDialer(val phone: String): ProfileContactsEvent
    data class LaunchEmail(val email: String): ProfileContactsEvent
}
