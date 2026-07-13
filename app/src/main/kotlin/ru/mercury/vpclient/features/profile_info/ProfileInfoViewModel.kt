package ru.mercury.vpclient.features.profile_info

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModel @Inject constructor(
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<ProfileInfoIntent, ProfileInfoModel, Event>(ProfileInfoModel()) {

    init {
        dispatch(ProfileInfoIntent.CollectCartCount)
        dispatch(ProfileInfoIntent.CollectFittingCount)
        dispatch(ProfileInfoIntent.CollectActiveEmployee)
        dispatch(ProfileInfoIntent.LoadCartData)
    }

    override fun dispatch(intent: ProfileInfoIntent) {
        when (intent) {
            is ProfileInfoIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is ProfileInfoIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is ProfileInfoIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is ProfileInfoIntent.LoadCartData -> {
                launch {
                    val badge = runCatching { cartBadgeUseCase(Unit).getOrThrow() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileInfoIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
            is ProfileInfoIntent.PaymentClick -> launch { ProfileRootEventManager.send(ProfilePaymentRoute) }
            is ProfileInfoIntent.DeliveryClick -> launch { ProfileRootEventManager.send(ProfileDeliveryRoute) }
            is ProfileInfoIntent.ReturnClick -> launch { ProfileRootEventManager.send(ProfileReturnRoute) }
            is ProfileInfoIntent.PrivacyPolicyClick -> launch { ProfileRootEventManager.send(ProfilePolicyRoute) }
            is ProfileInfoIntent.GiftCardClick -> launch { ProfileRootEventManager.send(ProfileGiftRoute) }
            is ProfileInfoIntent.ContactsClick -> launch { ProfileRootEventManager.send(ProfileContactsRoute) }
            is ProfileInfoIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileInfoIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is ProfileInfoIntent.MessengerClick -> return
        }
    }
}
