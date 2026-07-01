@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import javax.inject.Inject

// fixme
class ResetFilterValuesQuantityUseCase @Inject constructor(
    private val filterValuesQuantityDao: FilterValuesQuantityDao,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(chipId: String) {
        val entity = FilterValuesQuantityEntity(chipId = chipId, quantity = null)
        filterValuesQuantityDao.upsert(entity)
    }
}
