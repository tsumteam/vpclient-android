@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.database.dao.ActivityCounterDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ActivityCounterEntity
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import javax.inject.Inject

class ActivityCounterFlowUseCase @Inject constructor(
    private val activityCounterDao: ActivityCounterDao,
    dispatchers: SharedDispatchers
): FlowUseCase<ActivityCounterType, ActivityCounterEntity>(dispatchers.io) {

    override fun execute(type: ActivityCounterType): Flow<ActivityCounterEntity> {
        return activityCounterDao.selectFlow(type.name)
            .map { counter -> counter.orEmpty(type.name) }
    }
}
