package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import javax.inject.Inject

// fixme
class ViewHistoryProductsFlowUseCase @Inject constructor(
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<CatalogFilterProductsEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<CatalogFilterProductsEntity>> {
        return catalogViewHistoryProductDao.selectFlow()
    }
}
