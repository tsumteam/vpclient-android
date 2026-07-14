@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.MainScreenSectionsRequest
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.MainScreenSectionDao
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.homeSectionEntities
import ru.mercury.vpclient.shared.domain.mapper.mainScreenSectionEntities
import ru.mercury.vpclient.shared.domain.mapper.mainScreenSectionItemEntities
import javax.inject.Inject

class MainScreenSectionsUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val mainScreenSectionDao: MainScreenSectionDao,
    dispatchers: SharedDispatchers
): UseCase<MainScreenCategoryType, Unit>(dispatchers.io) {

    override suspend fun execute(category: MainScreenCategoryType) {
        handleResponse(
            request = {
                val request = MainScreenSectionsRequest(category)
                networkService.mainScreenSections(request)
            },
            onSuccess = { response ->
                val homeSectionEntities = response.items.homeSectionEntities
                val sectionEntities = homeSectionEntities.mainScreenSectionEntities(category)
                val itemEntities = homeSectionEntities.mainScreenSectionItemEntities(category)
                appDatabase.withTransaction {
                    mainScreenSectionDao.deleteItems(category)
                    mainScreenSectionDao.delete(category)
                    mainScreenSectionDao.upsertSections(sectionEntities)
                    mainScreenSectionDao.upsertItems(itemEntities)
                }
            },
            onFailure = { error -> throw MainScreenSectionsException(error.message) }
        )
    }

    data class MainScreenSectionsException(
        override val message: String
    ): ClientException(message)
}
