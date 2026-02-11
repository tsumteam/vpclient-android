package ru.mercury.vpclient.features.login.model

import ru.mercury.vpclient.core.entity.PhoneValidationError
import ru.mercury.vpclient.core.mvi.Model

data class LoginModel(
    val phone: String = "",
    val phoneValidationError: PhoneValidationError? = null,
    val isLoading: Boolean = false,
    val agreementUri: String = "https://google.com" // fixme
): Model
