package ru.mercury.vpclient.shared.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.entity.CodeValidationError
import ru.mercury.vpclient.shared.entity.NameValidationError
import ru.mercury.vpclient.shared.entity.PhoneValidationError
import ru.mercury.vpclient.shared.persistence.database.entity.ClientEntity

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
