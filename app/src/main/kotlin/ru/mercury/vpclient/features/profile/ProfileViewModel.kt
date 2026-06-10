package ru.mercury.vpclient.features.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.profile.model.ProfileModel
import ru.mercury.vpclient.features.profile_info.navigation.ProfileInfoRoute
import ru.mercury.vpclient.features.profile_my_data.navigation.MyDataRoute
import ru.mercury.vpclient.features.profile_qr.navigation.ProfileQrRoute
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileIntent, ProfileModel, Event>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectCartSize)
        dispatch(ProfileIntent.CollectActiveEmployee)
        dispatch(ProfileIntent.CollectViewHistoryProducts)
        dispatch(ProfileIntent.LoadEmployees)
        dispatch(ProfileIntent.LoadCartData)
        dispatch(ProfileIntent.LoadViewHistoryProducts)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(ProfileIntent.LoadCartData)
                            }
                        }
                }
            }
            is ProfileIntent.CollectViewHistoryProducts -> {
                launch {
                    interactor.viewHistoryProductsFlow()
                        .distinctUntilChanged()
                        .collectLatest { products ->
                            reduce { it.copy(viewHistoryProducts = products) }
                        }
                }
            }
            is ProfileIntent.LoadEmployees -> launch { runCatching { interactor.syncEmployees() } }
            is ProfileIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileIntent.LoadViewHistoryProducts -> {
                launch {
                    reduce { it.copy(isViewHistoryLoading = true) }
                    runCatching {
                        interactor.loadViewHistoryProducts(VIEW_HISTORY_PRODUCTS_LIMIT)
                    }
                    reduce { it.copy(isViewHistoryLoading = false) }
                }
            }
            is ProfileIntent.NotificationClick -> return
            is ProfileIntent.AddLoyaltyCardClick -> return
            is ProfileIntent.MyDataClick -> launch { ProfileStackEventManager.send(MyDataRoute) }
            is ProfileIntent.PurchasesClick -> return
            is ProfileIntent.InformationClick -> launch { ProfileStackEventManager.send(ProfileInfoRoute) }
            is ProfileIntent.QrCodeClick -> launch { MainEventManager.send(ProfileQrRoute) }
            is ProfileIntent.ViewHistoryViewMoreClick -> return
            is ProfileIntent.ViewHistoryProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.productId, openedFromCart = true)) }
            }
            is ProfileIntent.ShowLogoutDialog -> reduce { it.copy(isLogoutDialogVisible = true) }
            is ProfileIntent.DismissLogoutDialog -> reduce { it.copy(isLogoutDialogVisible = false) }
            is ProfileIntent.Logout -> {
                val job = launch {
                    reduce { it.copy(isLogoutDialogVisible = false) }
                    interactor.logout()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(logoutJob = null) } }
                }
                reduce { it.copy(logoutJob = job) }
            }
            is ProfileIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is ProfileIntent.MessengerClick -> return
        }
    }

    private companion object {
        private const val VIEW_HISTORY_PRODUCTS_LIMIT = 11
    }
}
