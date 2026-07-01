package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDeliveryAddressDao
import javax.inject.Inject

class ClientAddressesFlowUseCase @Inject constructor(
    private val clientDeliveryAddressDao: ClientDeliveryAddressDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<ClientDeliveryAddressEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<ClientDeliveryAddressEntity>> {
        return clientDeliveryAddressDao.selectAllFlow()
    }
}
