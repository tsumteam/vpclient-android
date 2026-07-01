@file:OptIn(ExperimentalPagingApi::class)
@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.DEFAULT_PAGE_SIZE
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.paging.CatalogFilterProductsRemoteMediator
import javax.inject.Inject

// fixme
class CatalogFilterProductsPagingDataUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val pagingKeyDao: PagingKeyDao,
    dispatchers: SharedDispatchers
): FlowUseCase<CatalogFilterProductsData, PagingData<CatalogFilterProductsEntity>>(dispatchers.io) {

    override fun execute(data: CatalogFilterProductsData): Flow<PagingData<CatalogFilterProductsEntity>> {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId

        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = CatalogFilterProductsRemoteMediator(
                data = data,
                networkService = networkService,
                appDatabase = appDatabase,
                catalogCategoryDao = catalogCategoryDao,
                catalogFilterProductsDao = catalogFilterProductsDao,
                pagingKeyDao = pagingKeyDao
            ),
            pagingSourceFactory = {
                catalogFilterProductsDao.pagingSource(categoryId, titleCategoryId)
            }
        ).flow
    }
}
