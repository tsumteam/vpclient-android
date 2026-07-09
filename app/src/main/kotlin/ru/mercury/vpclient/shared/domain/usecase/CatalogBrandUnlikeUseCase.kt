package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.FavoriteBrandDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogBrandUnlikeUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val favoriteBrandDao: FavoriteBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<CatalogBrandUnlikeUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        val entity = favoriteBrandDao.selectNotNull(pairedUserId, params.categoryId, params.brandId)
        favoriteBrandDao.delete(pairedUserId, params.categoryId, params.brandId)

        handleResponse(
            request = {
                val request = CatalogBrandFavoriteRequest(
                    brandId = params.brandId,
                    categoryId = params.categoryId
                )
                networkService.catalogBrandsUnlike(request)
            },
            onFailure = { error ->
                favoriteBrandDao.upsert(entity)
                throw CatalogBrandUnlikeException(error.message)
            }
        )
    }

    data class CatalogBrandUnlikeException(
        override val message: String
    ): ClientException(message)

    data class Params(
        val brandId: Int,
        val categoryId: Int
    )
}
