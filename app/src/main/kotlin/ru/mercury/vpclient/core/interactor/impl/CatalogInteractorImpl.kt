package ru.mercury.vpclient.core.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.core.coroutines.ClientDispatchers
import ru.mercury.vpclient.core.entity.CatalogData
import ru.mercury.vpclient.core.interactor.CatalogInteractor
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.core.repository.CatalogRepository
import javax.inject.Inject

class CatalogInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val catalogRepository: CatalogRepository
): CatalogInteractor {

    override val catalogDataFlow: Flow<CatalogData> = catalogRepository.catalogDataFlow

    override fun catalogCategoryFlow(id: Int): Flow<CatalogCategoryEntity> {
        return catalogRepository.catalogCategoryFlow(id)
    }

    override fun subcategoryPojosFlow(parentId: Int): Flow<List<SubcategoryPojo>> {
        return catalogRepository.subcategoryPojosFlow(parentId)
    }

    override suspend fun loadCatalogCategoriesBasic() {
        withContext(dispatchers.io) { catalogRepository.loadCatalogCategoriesBasic() }
    }

    override suspend fun loadCatalogCategoriesTop() {
        withContext(dispatchers.io) { catalogRepository.loadCatalogCategoriesTop() }
    }

    override suspend fun loadCatalogCategoriesBottom(parentCategoryId: Int) {
        withContext(dispatchers.io) { catalogRepository.loadCatalogCategoriesBottom(parentCategoryId) }
    }
}
