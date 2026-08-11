package ru.mercury.vpclient.features.auth_login.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.domain.usecase.AuthValidatePhoneUseCase.PhoneValidationError
import ru.mercury.vpclient.shared.mvi.Model

data class LoginModel(
    val phone: String = "",
    val phoneValidationError: PhoneValidationError? = null,
    val loginJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = loginJob?.isActive == true

    val isLoginEnabled: Boolean
        get() = !isLoading

    val isPhoneValidationErrorVisible: Boolean
        get() = phoneValidationError != null

    val isLoadingIndicatorVisible: Boolean
        get() = isLoading
}
