package ru.mercury.vpclient.features.checkout_payment_result

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.checkout_payment_result.intent.CheckoutPaymentResultIntent
import ru.mercury.vpclient.features.checkout_payment_result.model.CheckoutPaymentResultModel
import ru.mercury.vpclient.features.checkout_payment_result.navigation.CheckoutPaymentResultRoute
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.MainTab

@HiltViewModel(assistedFactory = CheckoutPaymentResultViewModelFactory::class)
class CheckoutPaymentResultViewModel @AssistedInject constructor(
    @Assisted route: CheckoutPaymentResultRoute
): ClientViewModel<CheckoutPaymentResultIntent, CheckoutPaymentResultModel, Event>(
    CheckoutPaymentResultModel(
        status = route.status,
        deliveryInterval = route.deliveryInterval,
        address = route.address,
        itemsCount = route.itemsCount
    )
) {

    override fun dispatch(intent: CheckoutPaymentResultIntent) {
        when (intent) {
            is CheckoutPaymentResultIntent.PurchasesClick -> {
                launch {
                    MainEventManager.send(
                        MainRoute(
                            popUpToMain = true,
                            selectedTab = MainTab.PROFILE_ORDERS
                        )
                    )
                }
            }
            is CheckoutPaymentResultIntent.CatalogClick -> {
                launch {
                    MainEventManager.send(
                        MainRoute(
                            popUpToMain = true,
                            selectedTab = MainTab.CATALOG
                        )
                    )
                }
            }
        }
    }
}

@AssistedFactory
interface CheckoutPaymentResultViewModelFactory {
    fun create(route: CheckoutPaymentResultRoute): CheckoutPaymentResultViewModel
}
