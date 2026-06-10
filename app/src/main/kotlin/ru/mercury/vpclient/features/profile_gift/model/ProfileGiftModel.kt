package ru.mercury.vpclient.features.profile_gift.model

import ru.mercury.vpclient.shared.data.PROFILE_GIFT_URL
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileGiftModel(
    val url: String = PROFILE_GIFT_URL
): Model
