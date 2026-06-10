package ru.mercury.vpclient.features.profile_policy.model

import ru.mercury.vpclient.shared.data.PROFILE_POLICY_URL
import ru.mercury.vpclient.shared.mvi.Model

data class ProfilePolicyModel(
    val url: String = PROFILE_POLICY_URL
): Model
