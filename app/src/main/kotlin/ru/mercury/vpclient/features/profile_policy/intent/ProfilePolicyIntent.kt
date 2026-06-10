package ru.mercury.vpclient.features.profile_policy.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfilePolicyIntent: Intent {
    data object BackClick: ProfilePolicyIntent
}
