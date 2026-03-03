package ru.mercury.vpclient.core.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.core.CODE_LENGTH
import ru.mercury.vpclient.core.coroutines.VPClientDispatchers
import ru.mercury.vpclient.core.entity.CodeValidationError
import ru.mercury.vpclient.core.entity.NameValidationError
import ru.mercury.vpclient.core.entity.PhoneValidationError
import ru.mercury.vpclient.core.interactor.AuthenticationInteractor
import ru.mercury.vpclient.core.ktx.isValidPhoneNumber
import ru.mercury.vpclient.core.ktx.normalizePhoneInput
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.core.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationInteractorImpl @Inject constructor(
    private val dispatchers: VPClientDispatchers,
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
