package ru.mercury.vpclient.features.profile_contacts

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_contacts.event.ProfileContactsEvent
import ru.mercury.vpclient.features.profile_contacts.intent.ProfileContactsIntent
import ru.mercury.vpclient.features.profile_contacts.model.ProfileContactsModel
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.data.CLIENT_SERVICE_EMAIL
import ru.mercury.vpclient.shared.data.CLIENT_SERVICE_PHONE
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.EmployeeInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileContactsViewModel @Inject constructor(
    private val employeeInteractor: EmployeeInteractor
): ClientViewModel<ProfileContactsIntent, ProfileContactsModel, ProfileContactsEvent>(ProfileContactsModel()) {

    init {
        dispatch(ProfileContactsIntent.CollectActiveEmployee)
    }

    override fun dispatch(intent: ProfileContactsIntent) {
        when (intent) {
            is ProfileContactsIntent.CollectActiveEmployee -> {
                launch {
                    employeeInteractor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } ?: EmployeeEntity.Empty }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                        }
                }
            }
            is ProfileContactsIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
            is ProfileContactsIntent.ConsultantPhoneClick -> {
                launch { send(ProfileContactsEvent.OpenDialer(stateFlow.value.consultantPhone)) }
            }
            is ProfileContactsIntent.CustomerServicePhoneClick -> {
                launch { send(ProfileContactsEvent.OpenDialer(CLIENT_SERVICE_PHONE)) }
            }
            is ProfileContactsIntent.CustomerServiceEmailClick -> {
                launch { send(ProfileContactsEvent.OpenEmail(CLIENT_SERVICE_EMAIL)) }
            }
        }
    }
}
