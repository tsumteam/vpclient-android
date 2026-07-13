package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogBrandDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogBrandFavoriteUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val catalogBrandDao: CatalogBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<CatalogBrandFavoriteUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        catalogBrandDao.updateFavorite(
            pairedUserId = pairedUserId,
            categoryId = params.categoryId,
            brandIds = setOf(params.brandId),
            isFavorite = params.isFavorite
        )

        handleResponse(
            request = {
                val request = CatalogBrandFavoriteRequest(
                    brandId = params.brandId,
                    categoryId = params.categoryId
                )
                when {
                    params.isFavorite -> networkService.catalogBrandsLike(request)
                    else -> networkService.catalogBrandsUnlike(request)
                }
            },
            onFailure = { error ->
                catalogBrandDao.updateFavorite(
                    pairedUserId = pairedUserId,
                    categoryId = params.categoryId,
                    brandIds = setOf(params.brandId),
                    isFavorite = !params.isFavorite
                )
                throw CatalogBrandFavoriteException(error.message)
            }
        )
    }

    data class CatalogBrandFavoriteException(
        override val message: String
    ): ClientException(message)

    data class Params(
        val brandId: Int,
        val categoryId: Int,
        val isFavorite: Boolean
    )
}
