package ru.mercury.vpclient.core.interactor.impl

import kotlinx.coroutines.withContext
import ru.mercury.vpclient.core.coroutines.VPClientDispatchers
import ru.mercury.vpclient.core.interactor.AuthenticationInteractor
import ru.mercury.vpclient.core.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationInteractorImpl @Inject constructor(
    private val dispatchers: VPClientDispatchers,
    private val authenticationRepository: AuthenticationRepository
): AuthenticationInteractor {

    override suspend fun register(phone: String, name: String): String {
        return withContext(dispatchers.io) { authenticationRepository.register(phone, name) }
    }

    override suspend fun login(phone: String): String {
        return withContext(dispatchers.io) { authenticationRepository.login(phone) }
    }

    override suspend fun continueLogin(phone: String, code: String): String {
        return withContext(dispatchers.io) { authenticationRepository.continueLogin(phone, code) }
    }

    override suspend fun logout() {
        withContext(dispatchers.io) { authenticationRepository.logout() }
    }
}
