package ru.mercury.vpclient.features.fitting_confirmation

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessRoute
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FittingConfirmationViewModel.Factory::class)
class FittingConfirmationViewModel @AssistedInject constructor(
    @Assisted route: FittingConfirmationRoute,
    private val cartInteractor: CartInteractor
): ClientViewModel<FittingConfirmationIntent, FittingConfirmationModel, Event>(FittingConfirmationModel(route)) {

    init {
        launch {
            cartInteractor.cartProductsFlow.collectLatest { products ->
                reduce { it.copy(products = products) }
            }
        }
    }

    override fun dispatch(intent: FittingConfirmationIntent) {
        when (intent) {
            is FittingConfirmationIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is FittingConfirmationIntent.ConfirmClick -> launch { MainEventManager.send(FittingSuccessRoute) }
            is FittingConfirmationIntent.SelectPlace -> reduce { it.copy(selectedPlaceIndex = intent.index) }
            is FittingConfirmationIntent.SelectDay -> reduce {
                it.copy(
                    selectedDayIndex = intent.index,
                    selectedInterval = null
                )
            }
            is FittingConfirmationIntent.SelectInterval -> reduce { it.copy(selectedInterval = intent.interval) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FittingConfirmationRoute): FittingConfirmationViewModel
    }
}
