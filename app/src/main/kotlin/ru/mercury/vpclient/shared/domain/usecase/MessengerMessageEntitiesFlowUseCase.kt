package ru.mercury.vpclient.shared.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity

class MessengerMessageEntitiesFlowUseCase @Inject constructor(
    private val messengerMessageDao: MessengerMessageDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<MessengerMessageEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<MessengerMessageEntity>> {
        return messengerMessageDao.selectFlow()
    }
}
