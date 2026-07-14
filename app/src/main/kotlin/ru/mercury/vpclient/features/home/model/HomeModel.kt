package ru.mercury.vpclient.features.home.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.HomePage
import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class HomeModel(
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val notificationCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val selectedTab: TabType = TabType.WOMAN,
    val pages: List<HomePage> = TabType.entries.map { tab -> HomePage(tab) },
    val loadedTabs: Set<TabType> = emptySet(),
    val loadMainScreenSectionsJobs: Map<TabType, Job> = emptyMap()
): Model {

    val currentHomeSectionEntities: List<HomeSectionEntity>
        get() = pages.first { page -> page.tab == selectedTab }.homeSectionEntities

    val isCurrentPageLoading: Boolean
        get() = selectedTab !in loadedTabs

    val isCurrentPageRefreshing: Boolean
        get() = selectedTab in loadedTabs && selectedTab in loadMainScreenSectionsJobs

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge

    val isNotificationBadgeVisible: Boolean
        get() = notificationCount > 0
}
