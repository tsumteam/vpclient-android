package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationInteractorImpl @Inject constructor(
    private val dispatchers: SharedDispatchers,
    private val authenticationRepository: AuthenticationRepository
): AuthenticationInteractor {

    override suspend fun currentUser(): CurrentUserResponse {
        return withContext(dispatchers.io) { authenticationRepository.currentUser() }
    }

    override suspend fun userId(): String {
        return withContext(dispatchers.io) { authenticationRepository.userId() }
    }

    override suspend fun deleteProfile() {
        withContext(dispatchers.io) { authenticationRepository.deleteProfile() }
    }

    override suspend fun logout() {
        withContext(dispatchers.io) { authenticationRepository.logout() }
    }
}
