package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class DeleteProfileUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val logoutUseCase: LogoutUseCase,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = { networkService.userProfileDelete() },
            onSuccess = { logoutUseCase(Unit).getOrThrow() },
            onEmpty = { logoutUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw DeleteProfileException(error.message) }
        )
    }

    data class DeleteProfileException(
        override val message: String
    ): ClientException(message)
}
