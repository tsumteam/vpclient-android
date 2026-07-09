@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandsFavoriteRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.FavoriteBrandDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogBrandFavoritesUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val favoriteBrandDao: FavoriteBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        handleResponse(
            request = {
                val request = CatalogBrandsFavoriteRequest(pairedUserId)
                networkService.catalogBrandsFavorites(request)
            },
            onSuccess = { response ->
                val entities = response.items.orEmpty().entities(pairedUserId)
                appDatabase.withTransaction {
                    favoriteBrandDao.delete(pairedUserId)
                    favoriteBrandDao.upsert(entities)
                }
            },
            onFailure = { error -> throw CatalogBrandFavoritesException(error.message) }
        )
    }

    data class CatalogBrandFavoritesException(
        override val message: String
    ): ClientException(message)
}
