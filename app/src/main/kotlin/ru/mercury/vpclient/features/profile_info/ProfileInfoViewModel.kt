package ru.mercury.vpclient.features.profile_info

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.profile_contacts.navigation.ProfileContactsRoute
import ru.mercury.vpclient.features.profile_delivery.navigation.ProfileDeliveryRoute
import ru.mercury.vpclient.features.profile_gift.navigation.ProfileGiftRoute
import ru.mercury.vpclient.features.profile_info.intent.ProfileInfoIntent
import ru.mercury.vpclient.features.profile_info.model.ProfileInfoModel
import ru.mercury.vpclient.features.profile_payment.navigation.ProfilePaymentRoute
import ru.mercury.vpclient.features.profile_policy.navigation.ProfilePolicyRoute
import ru.mercury.vpclient.features.profile_return.navigation.ProfileReturnRoute
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileInfoIntent, ProfileInfoModel, Event>(ProfileInfoModel()) {

    init {
        dispatch(ProfileInfoIntent.CollectCartSize)
        dispatch(ProfileInfoIntent.CollectActiveEmployee)
        dispatch(ProfileInfoIntent.LoadCartData)
    }

    override fun dispatch(intent: ProfileInfoIntent) {
        when (intent) {
            is ProfileInfoIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileInfoIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(ProfileInfoIntent.LoadCartData)
                            }
                        }
                }
            }
            is ProfileInfoIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileInfoIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
            is ProfileInfoIntent.PaymentClick -> launch { ProfileStackEventManager.send(ProfilePaymentRoute) }
            is ProfileInfoIntent.DeliveryClick -> launch { ProfileStackEventManager.send(ProfileDeliveryRoute) }
            is ProfileInfoIntent.ReturnClick -> launch { ProfileStackEventManager.send(ProfileReturnRoute) }
            is ProfileInfoIntent.PrivacyPolicyClick -> launch { ProfileStackEventManager.send(ProfilePolicyRoute) }
            is ProfileInfoIntent.GiftCardClick -> launch { ProfileStackEventManager.send(ProfileGiftRoute) }
            is ProfileInfoIntent.ContactsClick -> launch { ProfileStackEventManager.send(ProfileContactsRoute) }
            is ProfileInfoIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileInfoIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is ProfileInfoIntent.MessengerClick -> return
        }
    }
}
