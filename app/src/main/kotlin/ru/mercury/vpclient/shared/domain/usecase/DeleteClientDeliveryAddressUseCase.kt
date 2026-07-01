@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.DeleteClientAddressRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDeliveryAddressDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class DeleteClientDeliveryAddressUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val clientDeliveryAddressDao: ClientDeliveryAddressDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Int, Unit>(dispatchers.io) {

    override suspend fun execute(addressId: Int) {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()

        handleResponse(
            request = {
                val request = DeleteClientAddressRequest(
                    clientId = clientId,
                    addressId = addressId
                )
                networkService.clientAddressDelete(request)
            },
            onSuccess = { clientDeliveryAddressDao.delete(addressId) },
            onFailure = { error -> throw ClientAddressException(error.message) }
        )
    }

    data class ClientAddressException(
        override val message: String
    ): ClientException(message)
}
