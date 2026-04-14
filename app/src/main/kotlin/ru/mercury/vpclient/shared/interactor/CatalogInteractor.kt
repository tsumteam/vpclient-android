package ru.mercury.vpclient.shared.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.entity.CatalogData
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.persistence.database.pojo.SubcategoryPojo

interface CatalogInteractor {

    val catalogDataFlow: Flow<CatalogData>

    fun catalogCategoryFlow(id: Int): Flow<CatalogCategoryEntity>

    fun subcategoryPojosFlow(parentId: Int): Flow<List<SubcategoryPojo>>

    suspend fun loadCatalogCategoriesBasic()

    suspend fun loadCatalogCategoriesTop()

    suspend fun loadCatalogCategoriesBottom(parentCategoryId: Int)
}
