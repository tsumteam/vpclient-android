package ru.mercury.vpclient.shared.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.CODE_LENGTH
import ru.mercury.vpclient.shared.coroutines.ClientDispatchers
import ru.mercury.vpclient.shared.entity.CodeValidationError
import ru.mercury.vpclient.shared.entity.NameValidationError
import ru.mercury.vpclient.shared.entity.PhoneValidationError
import ru.mercury.vpclient.shared.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.ktx.isValidPhoneNumber
import ru.mercury.vpclient.shared.ktx.normalizePhoneInput
import ru.mercury.vpclient.shared.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val authenticationRepository: AuthenticationRepository
): AuthenticationInteractor {

    override val clientEntityFlow: Flow<ClientEntity> = authenticationRepository.clientEntityFlow

    override fun validateRequiredName(name: String): NameValidationError? {
        return when {
            name.isBlank() -> NameValidationError.Empty
            else -> null
        }
    }

    override fun validateRequiredPhone(phone: String): PhoneValidationError? {
        val normalizedPhone = normalizePhoneInput(phone)
        return when {
            normalizedPhone.isEmpty() -> PhoneValidationError.Empty
            !normalizedPhone.isValidPhoneNumber() -> PhoneValidationError.Invalid
            else -> null
        }
    }

    override fun validateRequiredCode(code: String): CodeValidationError? {
        return when {
            code.length < CODE_LENGTH -> CodeValidationError.Empty
            else -> null
        }
    }

    override suspend fun register(phone: String, name: String) {
        withContext(dispatchers.io) { authenticationRepository.register(phone, name) }
    }

    override suspend fun login(phone: String) {
        withContext(dispatchers.io) { authenticationRepository.login(phone) }
    }

    override suspend fun continueLogin(code: String) {
        withContext(dispatchers.io) { authenticationRepository.continueLogin(code) }
    }

    override suspend fun logout() {
        withContext(dispatchers.io) { authenticationRepository.logout() }
    }

    override suspend fun resendCode() {
        withContext(dispatchers.io) { authenticationRepository.resendCode() }
    }
}
