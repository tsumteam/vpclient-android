package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import javax.inject.Inject

class ClientEntityFlowUseCase @Inject constructor(
    private val clientDao: ClientDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, ClientEntity>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<ClientEntity> {
        return clientDao.selectFlow()
    }
}
