@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.mapper.viewType
import javax.inject.Inject

class CatalogFilterProductQuantityUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterProductsQuantityDao: CatalogFilterProductsQuantityDao,
    dispatchers: SharedDispatchers
): UseCase<CatalogFilterRequestData2, Unit>(dispatchers.io) {

    override suspend fun execute(data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId

        handleResponse(
            request = {
                val viewType = data.viewTypeOverride ?: catalogCategoryDao.selectNotNull(categoryId)
                    .viewType(categoryId, titleCategoryId)
                val request = FilteredProductsQuantityRequest(
                    viewType = viewType,
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(
                        categoryId = categoryId,
                        includeDefaultCategory = data.includeDefaultCategory
                    )
                )
                networkService.catalogFilterProductsQuantity(request)
            },
            onSuccess = { response ->
                val catalogFilterProductsQuantityEntity = CatalogFilterProductsQuantityEntity(
                    categoryId = categoryId,
                    titleCategoryId = titleCategoryId,
                    productsQuantity = response.quantity
                )
                appDatabase.withTransaction {
                    catalogFilterProductsQuantityDao.delete(categoryId, titleCategoryId)
                    catalogFilterProductsQuantityDao.upsert(catalogFilterProductsQuantityEntity)
                }
            }
        )
    }
}
