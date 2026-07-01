@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import javax.inject.Inject

// fixme
class FilterValuesEntityFlowUseCase @Inject constructor(
    private val filterValuesDao: FilterValuesDao,
    dispatchers: SharedDispatchers
): FlowUseCase<String, FilterValuesEntity>(dispatchers.io) {

    override fun execute(chipId: String): Flow<FilterValuesEntity> {
        return filterValuesDao.selectFlow(chipId).mapNotNull { it }
    }
}
