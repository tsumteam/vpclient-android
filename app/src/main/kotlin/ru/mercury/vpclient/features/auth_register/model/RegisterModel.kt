package ru.mercury.vpclient.features.auth_register.model

import ru.mercury.vpclient.shared.domain.usecase.AuthValidateNameUseCase.NameValidationError
import ru.mercury.vpclient.shared.domain.usecase.AuthValidatePhoneUseCase.PhoneValidationError
import ru.mercury.vpclient.shared.mvi.Model

data class RegisterModel(
    val name: String = "",
    val phone: String = "",
    val nameValidationError: NameValidationError? = null,
    val phoneValidationError: PhoneValidationError? = null,
    val isLoading: Boolean = false
): Model {

    val isRegisterEnabled: Boolean
        get() = !isLoading

    val isNameValidationErrorVisible: Boolean
        get() = nameValidationError != null

    val isPhoneValidationErrorVisible: Boolean
        get() = phoneValidationError != null

    val isLoadingIndicatorVisible: Boolean
        get() = isLoading
}
