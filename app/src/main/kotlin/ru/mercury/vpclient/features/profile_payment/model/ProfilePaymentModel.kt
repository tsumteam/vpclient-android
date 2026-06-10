package ru.mercury.vpclient.features.profile_payment.model

import ru.mercury.vpclient.shared.data.PROFILE_PAYMENT_URL
import ru.mercury.vpclient.shared.mvi.Model

data class ProfilePaymentModel(
    val url: String = PROFILE_PAYMENT_URL
): Model
