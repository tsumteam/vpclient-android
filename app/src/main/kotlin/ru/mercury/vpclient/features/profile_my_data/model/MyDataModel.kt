package ru.mercury.vpclient.features.profile_my_data.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.fittingText
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class MyDataModel(
    val deleteProfileJob: Job? = null,
    val cartSize: Int = 0,
    val cartBadge: Int = 0,
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
            cartSize > 0 -> cartSize.toString()
            else -> ""
        }

    val showCartBadge: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = activeEmployee.fittingText

    val showFittingButton: Boolean
        get() = activeEmployee.hasFittingProducts

    val showFittingBadge: Boolean
        get() = activeEmployee.hasFittingBadge

    val showMessengerBadge: Boolean
        get() = activeEmployee.hasMessengerBadge
}
