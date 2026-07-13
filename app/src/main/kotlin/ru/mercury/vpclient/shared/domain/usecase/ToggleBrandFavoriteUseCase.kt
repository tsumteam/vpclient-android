package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogBrandDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class ToggleBrandFavoriteUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogBrandDao: CatalogBrandDao,
    private val filterValuesDao: FilterValuesDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<ToggleBrandFavoriteUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        val pickerChipId = params.chipId.substringBefore("_")
        val entity = filterValuesDao.select(pickerChipId)
        val updatedEntity = entity?.copy(
            items = entity.items.map { item ->
                if (item.requestValue == params.brandId.toString()) item.copy(isFavorite = params.isFavorite) else item
            }
        )

        handleResponse(
            request = {
                val request = CatalogBrandFavoriteRequest(
                    brandId = params.brandId,
                    categoryId = params.categoryId
                )
                if (params.isFavorite) networkService.catalogBrandsLike(request)
                else networkService.catalogBrandsUnlike(request)
            },
            onSuccess = {
                when {
                    updatedEntity != null -> {
                        appDatabase.withTransaction {
                            filterValuesDao.upsert(updatedEntity)
                            catalogBrandDao.updateFavorite(
                                pairedUserId = pairedUserId,
                                categoryId = params.categoryId,
                                brandIds = setOf(params.brandId),
                                isFavorite = params.isFavorite
                            )
                        }
                    }
                    else -> {
                        catalogBrandDao.updateFavorite(
                            pairedUserId = pairedUserId,
                            categoryId = params.categoryId,
                            brandIds = setOf(params.brandId),
                            isFavorite = params.isFavorite
                        )
                    }
                }
            },
            onFailure = { error -> throw ToggleBrandFavoriteException(error.message) }
        )
    }

    data class ToggleBrandFavoriteException(
        override val message: String
    ): ClientException(message)

    data class Params(
        val chipId: String,
        val brandId: Int,
        val categoryId: Int,
        val isFavorite: Boolean
    )
}
