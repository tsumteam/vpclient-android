@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import javax.inject.Inject

// fixme
class CatalogFilterProductFlowUseCase @Inject constructor(
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    dispatchers: SharedDispatchers
): FlowUseCase<String, CatalogFilterProductsEntity>(dispatchers.io) {

    override fun execute(id: String): Flow<CatalogFilterProductsEntity> {
        return catalogFilterProductsDao.selectFlow(id)
    }
}
