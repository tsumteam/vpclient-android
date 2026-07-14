@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.dao.MainScreenSectionDao
import ru.mercury.vpclient.shared.domain.mapper.homeSectionEntity
import javax.inject.Inject

class HomeSectionEntitiesFlowUseCase @Inject constructor(
    private val mainScreenSectionDao: MainScreenSectionDao,
    dispatchers: SharedDispatchers
): FlowUseCase<MainScreenCategoryType, List<HomeSectionEntity>>(dispatchers.io) {

    override fun execute(category: MainScreenCategoryType): Flow<List<HomeSectionEntity>> {
        return mainScreenSectionDao.selectFlow(category).map { sections ->
            sections.map { section -> section.homeSectionEntity() }
        }
    }
}
