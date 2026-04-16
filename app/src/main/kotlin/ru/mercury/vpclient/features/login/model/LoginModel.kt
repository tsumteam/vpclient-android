package ru.mercury.vpclient.features.login.model

import ru.mercury.vpclient.shared.data.entity.PhoneValidationError
import ru.mercury.vpclient.shared.mvi.Model

data class LoginModel(
    val phone: String = "",
    val phoneValidationError: PhoneValidationError? = null,
    val isLoading: Boolean = false
): Model {

    val isLoginEnabled: Boolean
        get() = !isLoading
}
