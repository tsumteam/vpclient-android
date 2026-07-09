package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogBrandDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogBrandsSaveFavoritesUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogBrandDao: CatalogBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<CatalogBrandsSaveFavoritesUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        params.addedBrandIds.forEach { brandId ->
            handleResponse(
                request = {
                    val request = CatalogBrandFavoriteRequest(
                        brandId = brandId,
                        categoryId = params.categoryId
                    )
                    networkService.catalogBrandsLike(request)
                },
                onFailure = { error -> throw CatalogBrandsSaveFavoritesException(error.message) }
            )
        }
        params.removedBrandIds.forEach { brandId ->
            handleResponse(
                request = {
                    val request = CatalogBrandFavoriteRequest(
                        brandId = brandId,
                        categoryId = params.categoryId
                    )
                    networkService.catalogBrandsUnlike(request)
                },
                onFailure = { error -> throw CatalogBrandsSaveFavoritesException(error.message) }
            )
        }

        when {
            params.addedBrandIds.isNotEmpty() && params.removedBrandIds.isNotEmpty() -> {
                appDatabase.withTransaction {
                    catalogBrandDao.updateFavorite(pairedUserId, params.categoryId, params.addedBrandIds, isFavorite = true)
                    catalogBrandDao.updateFavorite(pairedUserId, params.categoryId, params.removedBrandIds, isFavorite = false)
                }
            }
            params.addedBrandIds.isNotEmpty() -> {
                catalogBrandDao.updateFavorite(pairedUserId, params.categoryId, params.addedBrandIds, isFavorite = true)
            }
            params.removedBrandIds.isNotEmpty() -> {
                catalogBrandDao.updateFavorite(pairedUserId, params.categoryId, params.removedBrandIds, isFavorite = false)
            }
        }
    }

    data class CatalogBrandsSaveFavoritesException(
        override val message: String
    ): ClientException(message)

    data class Params(
        val categoryId: Int,
        val addedBrandIds: Set<Int>,
        val removedBrandIds: Set<Int>
    )
}
