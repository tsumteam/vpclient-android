package ru.mercury.vpclient.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CatalogData
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo

interface CatalogRepository {

    val catalogDataFlow: Flow<CatalogData>

    fun catalogCategoryFlow(id: Int): Flow<CatalogCategoryEntity>

    fun subcategoryPojosFlow(parentId: Int): Flow<List<SubcategoryPojo>>

    suspend fun loadCatalogCategoriesBasic()

    suspend fun loadCatalogCategoriesTop()

    suspend fun loadCatalogCategoriesBottom(parentCategoryId: Int)
}
