package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateNameUseCase.NameValidationError
import javax.inject.Inject

class AuthValidateNameUseCase @Inject constructor(
    dispatchers: SharedDispatchers
): UseCase<String, NameValidationError?>(dispatchers.io) {

    override suspend fun execute(params: String): NameValidationError? {
        return when {
            params.isBlank() -> NameValidationError.Empty
            else -> null
        }
    }

    sealed interface NameValidationError {
        data object Empty: NameValidationError
    }
}
