package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.CreateClientAddressRequest
import ru.mercury.vpclient.shared.data.network.request.UpdateClientAddressRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDeliveryAddressDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.clientAddressBaseDto
import ru.mercury.vpclient.shared.domain.mapper.clientAddressDto
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddress
import ru.mercury.vpclient.shared.domain.mapper.coordinateDto
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class SaveClientDeliveryAddressUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val clientDeliveryAddressDao: ClientDeliveryAddressDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<SaveClientDeliveryAddressUseCase.Params, ClientDeliveryAddressEntity>(dispatchers.io) {

    override suspend fun execute(params: Params): ClientDeliveryAddressEntity {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
        val savedAddress = when {
            params.isEdit -> updateAddress(clientId, params.address)
            else -> createAddress(clientId, params.address)
        }
        clientDeliveryAddressDao.upsert(savedAddress)

        return savedAddress
    }

    private suspend fun createAddress(
        clientId: String,
        address: ClientDeliveryAddressEntity
    ): ClientDeliveryAddressEntity {
        val result = handleResponseResult(
            request = {
                val request = CreateClientAddressRequest(
                    clientId = clientId,
                    address = address.clientAddressBaseDto,
                    coordinate = address.coordinateDto
                )
                networkService.clientAddressCreate(request)
            }
        ).getOrThrow()

        return result.clientDeliveryAddress(address)
    }

    private suspend fun updateAddress(
        clientId: String,
        address: ClientDeliveryAddressEntity
    ): ClientDeliveryAddressEntity {
        val result = handleResponseResult(
            request = {
                val request = UpdateClientAddressRequest(
                    clientId = clientId,
                    address = address.clientAddressDto,
                    coordinate = address.coordinateDto
                )
                networkService.clientAddressUpdate(request)
            }
        ).getOrThrow()

        return result.clientDeliveryAddress(address)
    }

    data class Params(
        val address: ClientDeliveryAddressEntity,
        val isEdit: Boolean
    )
}
