package ru.mercury.vpclient.features.profile_contacts.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileContactsIntent: Intent {
    data object CollectActiveEmployee: ProfileContactsIntent
    data object BackClick: ProfileContactsIntent
    data object ConsultantPhoneClick: ProfileContactsIntent
    data object CustomerServicePhoneClick: ProfileContactsIntent
    data object CustomerServiceEmailClick: ProfileContactsIntent
}
