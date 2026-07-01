package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDeliveryAddressDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddressEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class ClientAddressListUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val database: AppDatabase,
    private val clientDeliveryAddressDao: ClientDeliveryAddressDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
        if (clientId.isEmpty()) return

        handleResponse(
            request = {
                val request = GetListClientAddressRequest(clientId)
                networkService.clientAddressList(request)
            },
            onSuccess = { result ->
                val addresses = result.items.orEmpty().mapNotNull { address ->
                    address.clientDeliveryAddressEntity
                }
                database.withTransaction {
                    clientDeliveryAddressDao.delete()
                    clientDeliveryAddressDao.upsert(addresses)
                }
            },
            onFailure = { error -> throw ClientAddressListException(error.message) }
        )
    }

    data class ClientAddressListException(
        override val message: String
    ): ClientException(message)
}
