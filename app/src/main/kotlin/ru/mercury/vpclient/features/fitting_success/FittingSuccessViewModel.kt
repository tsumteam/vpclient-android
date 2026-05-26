package ru.mercury.vpclient.features.fitting_success

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.fitting_success.intent.FittingSuccessIntent
import ru.mercury.vpclient.features.fitting_success.model.FittingSuccessModel
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class FittingSuccessViewModel @Inject constructor(): ClientViewModel<FittingSuccessIntent, FittingSuccessModel, Event>(FittingSuccessModel()) {

    override fun dispatch(intent: FittingSuccessIntent) {
        when (intent) {
            is FittingSuccessIntent.CatalogClick -> {
                launch { MainEventManager.send(MainRoute(popUpToMain = true, selectedTab = MainRoute.CATALOG_TAB)) }
            }
            is FittingSuccessIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
        }
    }
}
