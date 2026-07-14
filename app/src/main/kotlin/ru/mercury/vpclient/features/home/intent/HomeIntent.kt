package ru.mercury.vpclient.features.home.intent

import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionItemEntity
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface HomeIntent: Intent {
    data object CollectCartCount: HomeIntent
    data object CollectFittingCount: HomeIntent
    data object CollectActiveEmployee: HomeIntent
    data object CollectNotificationCount: HomeIntent
    data object LoadCartData: HomeIntent
    data object SearchClick: HomeIntent
    data object NotificationClick: HomeIntent
    data object CartClick: HomeIntent
    data object FittingClick: HomeIntent
    data object MessengerClick: HomeIntent
    data object CollectMainScreenSections: HomeIntent
    data class LoadMainScreenSections(val tab: TabType): HomeIntent
    data class SelectTab(val tab: TabType): HomeIntent
    data class ProductClick(val item: HomeSectionItemEntity): HomeIntent
    data class SectionItemClick(val item: HomeSectionItemEntity): HomeIntent
    data class SectionViewMoreClick(val section: HomeSectionEntity): HomeIntent
}
