@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import javax.inject.Inject

class CatalogFilterProductsEntitiesFlowUseCase @Inject constructor(
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Int, List<CatalogFilterProductsEntity>>(dispatchers.io) {

    override fun execute(categoryId: Int): Flow<List<CatalogFilterProductsEntity>> {
        return catalogFilterProductsDao.selectFlow(categoryId)
    }
}
