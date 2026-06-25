@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import javax.inject.Inject

class CatalogCategoryFlowUseCase @Inject constructor(
    private val catalogCategoryDao: CatalogCategoryDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Int, CatalogCategoryEntity>(dispatchers.io) {

    override fun execute(categoryId: Int): Flow<CatalogCategoryEntity> {
        return catalogCategoryDao.selectFlow(categoryId)
    }
}
