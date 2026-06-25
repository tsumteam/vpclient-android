@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo
import javax.inject.Inject

class CatalogSubcategoryPojosFlowUseCase @Inject constructor(
    private val catalogCategoryDao: CatalogCategoryDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Int, List<SubcategoryPojo>>(dispatchers.io) {

    override fun execute(parentCategoryId: Int): Flow<List<SubcategoryPojo>> {
        return catalogCategoryDao.selectPojosFlow(parentCategoryId)
    }
}
