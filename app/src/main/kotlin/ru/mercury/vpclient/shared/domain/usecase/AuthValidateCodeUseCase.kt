package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.CodeValidationError
import javax.inject.Inject

class AuthValidateCodeUseCase @Inject constructor(
    dispatchers: SharedDispatchers
): UseCase<String, CodeValidationError?>(dispatchers.io) {

    override suspend fun execute(params: String): CodeValidationError? {
        return when {
            params.length < CODE_LENGTH -> CodeValidationError.Empty
            else -> null
        }
    }

    sealed interface CodeValidationError {
        data object Empty: CodeValidationError
        data object Invalid: CodeValidationError
    }

    companion object {
        const val CODE_LENGTH = 6
    }
}
