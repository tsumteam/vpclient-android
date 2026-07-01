@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import javax.inject.Inject

// fixme
class FilterValuesQuantityEntityFlowUseCase @Inject constructor(
    private val filterValuesQuantityDao: FilterValuesQuantityDao,
    dispatchers: SharedDispatchers
): FlowUseCase<String, FilterValuesQuantityEntity>(dispatchers.io) {

    override fun execute(chipId: String): Flow<FilterValuesQuantityEntity> {
        return filterValuesQuantityDao.selectFlow(chipId).mapNotNull { it }
    }
}
