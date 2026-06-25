package ru.mercury.vpclient.features.profile_my_data.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileMyDataModel(
    val deleteProfileJob: Job? = null,
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val surname: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val isDeleteProfileDialogVisible: Boolean = false,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val isDeleteProfileLoading: Boolean
        get() = deleteProfileJob?.isActive == true

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
}
