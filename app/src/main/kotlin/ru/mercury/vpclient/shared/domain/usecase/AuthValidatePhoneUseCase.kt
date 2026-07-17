package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.domain.mapper.isValidPhoneNumber
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.usecase.AuthValidatePhoneUseCase.PhoneValidationError
import javax.inject.Inject

class AuthValidatePhoneUseCase @Inject constructor(
    dispatchers: SharedDispatchers
): UseCase<String, PhoneValidationError?>(dispatchers.io) {

    override suspend fun execute(params: String): PhoneValidationError? {
        val normalizedPhone = normalizePhoneInput(params)
        return when {
            normalizedPhone.isEmpty() -> PhoneValidationError.Empty
            !normalizedPhone.isValidPhoneNumber() -> PhoneValidationError.Invalid
            else -> null
        }
    }

    sealed interface PhoneValidationError {
        data object Empty: PhoneValidationError
        data object Invalid: PhoneValidationError
    }
}
