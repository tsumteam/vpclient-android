package ru.mercury.vpclient.features.profile_return.model

import ru.mercury.vpclient.shared.data.PROFILE_RETURN_URL
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileReturnModel(
    val url: String = PROFILE_RETURN_URL
): Model
