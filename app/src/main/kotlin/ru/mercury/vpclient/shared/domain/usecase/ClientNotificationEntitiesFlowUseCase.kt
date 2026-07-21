@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientNotificationDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientNotificationEntity

class ClientNotificationEntitiesFlowUseCase @Inject constructor(
    private val clientNotificationDao: ClientNotificationDao,
    dispatchers: SharedDispatchers
): FlowUseCase<ClientNotificationCategory, List<ClientNotificationEntity>>(dispatchers.io) {

    override fun execute(category: ClientNotificationCategory): Flow<List<ClientNotificationEntity>> {
        return clientNotificationDao.selectFlow(category)
    }
}
