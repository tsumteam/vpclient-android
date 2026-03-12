package ru.mercury.vpclient.core.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.entity.CatalogScreenData
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.SubcategoryPojo

interface CatalogInteractor {

    val catalogScreenDataFlow: Flow<CatalogScreenData>

    fun catalogCategoryFlow(id: Int): Flow<CatalogCategoryEntity>

    fun subcategoryPojosFlow(parentId: Int): Flow<List<SubcategoryPojo>>

    suspend fun loadCatalogCategoriesBasic()

    suspend fun loadCatalogCategoriesTop()

    suspend fun loadCatalogCategoriesBottom(parentCategoryId: Int)
}
