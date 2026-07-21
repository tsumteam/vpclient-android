package ru.mercury.vpclient.features.notifications.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientNotificationEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.basketText
import ru.mercury.vpclient.shared.domain.mapper.fittingText
import ru.mercury.vpclient.shared.domain.mapper.hasBasketBadge
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class NotificationsModel(
    val notificationEntities: List<ClientNotificationEntity> = emptyList(),
    val selectedCategory: ClientNotificationCategory = ClientNotificationCategory.ALL,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val collectNotificationsJob: Job? = null,
    val clientNotificationsJob: Job? = null,
    val refreshNotificationsJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = notificationEntities.isEmpty() && clientNotificationsJob?.isActive == true

    val isRefreshing: Boolean
        get() = notificationEntities.isNotEmpty() && refreshNotificationsJob?.isActive == true

    val isPullToRefreshEnabled: Boolean
        get() = clientNotificationsJob?.isActive != true

    val isContentVisible: Boolean
        get() = notificationEntities.isNotEmpty()

    val isEmptyVisible: Boolean
        get() = notificationEntities.isEmpty() &&
            clientNotificationsJob == null &&
            refreshNotificationsJob == null

    val cartText: String
        get() = activeEmployee.basketText

    val fittingText: String
        get() = activeEmployee.fittingText

    val isCartBadgeVisible: Boolean
        get() = activeEmployee.hasBasketBadge

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge
}
