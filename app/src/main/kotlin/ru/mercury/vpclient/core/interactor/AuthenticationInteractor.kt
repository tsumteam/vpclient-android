package ru.mercury.vpclient.core.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.entity.CodeValidationError
import ru.mercury.vpclient.core.entity.NameValidationError
import ru.mercury.vpclient.core.entity.PhoneValidationError
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

interface AuthenticationInteractor {

    val clientEntityFlow: Flow<ClientEntity>

    fun validateRequiredName(name: String): NameValidationError?

    fun validateRequiredPhone(phone: String): PhoneValidationError?

    fun validateRequiredCode(code: String): CodeValidationError?

    suspend fun register(phone: String, name: String)

    suspend fun login(phone: String)

    suspend fun continueLogin(code: String)

    suspend fun logout()

    suspend fun resendCode()
}
