package ru.mercury.vpclient.features.gift_card_result

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.gift_card_result.intent.GiftCardResultIntent
import ru.mercury.vpclient.features.gift_card_result.model.GiftCardResultModel
import ru.mercury.vpclient.features.gift_card_result.navigation.GiftCardResultRoute
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.MainTab

@HiltViewModel(assistedFactory = GiftCardResultViewModelFactory::class)
class GiftCardResultViewModel @AssistedInject constructor(
    @Assisted route: GiftCardResultRoute
): ClientViewModel<GiftCardResultIntent, GiftCardResultModel, Event>(
    GiftCardResultModel(
        isPaid = route.isPaid,
        orderNumber = route.orderNumber,
        email = route.email,
        phone = route.phone,
        deliveryDate = route.deliveryDate,
        deliveryTime = route.deliveryTime
    )
) {

    override fun dispatch(intent: GiftCardResultIntent) {
        when (intent) {
            is GiftCardResultIntent.HomeClick -> {
                launch {
                    MainEventManager.send(
                        MainRoute(
                            popUpToMain = true,
                            selectedTab = MainTab.HOME
                        )
                    )
                }
            }
            is GiftCardResultIntent.PurchasesClick -> {
                launch {
                    MainEventManager.send(
                        MainRoute(
                            popUpToMain = true,
                            selectedTab = MainTab.PROFILE_ORDERS
                        )
                    )
                }
            }
        }
    }
}

@AssistedFactory
interface GiftCardResultViewModelFactory {
    fun create(route: GiftCardResultRoute): GiftCardResultViewModel
}
