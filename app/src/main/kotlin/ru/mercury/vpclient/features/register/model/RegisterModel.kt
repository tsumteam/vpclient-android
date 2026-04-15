package ru.mercury.vpclient.features.register.model

import ru.mercury.vpclient.shared.data.entity.NameValidationError
import ru.mercury.vpclient.shared.data.entity.PhoneValidationError
import ru.mercury.vpclient.shared.domain.mapper.isValidPhoneNumber
import ru.mercury.vpclient.shared.mvi.Model

data class RegisterModel(
    val name: String = "",
    val phone: String = "",
    val nameValidationError: NameValidationError? = null,
    val phoneValidationError: PhoneValidationError? = null,
    val isLoading: Boolean = false
): Model {

    val isRegisterEnabled: Boolean
        get() = name.isNotBlank() && phone.isValidPhoneNumber() && !isLoading
}
