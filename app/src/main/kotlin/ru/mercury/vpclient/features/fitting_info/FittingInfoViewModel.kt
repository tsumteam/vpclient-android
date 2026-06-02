package ru.mercury.vpclient.features.fitting_info

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_info.intent.FittingInfoIntent
import ru.mercury.vpclient.features.fitting_info.model.FittingInfoModel
import ru.mercury.vpclient.features.fitting_info.navigation.FittingInfoRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FittingInfoViewModel.Factory::class)
class FittingInfoViewModel @AssistedInject constructor(
    @Assisted private val route: FittingInfoRoute
): ClientViewModel<FittingInfoIntent, FittingInfoModel, Event>(FittingInfoModel()) {

    init {
        dispatch(FittingInfoIntent.Init)
    }

    override fun dispatch(intent: FittingInfoIntent) {
        when (intent) {
            is FittingInfoIntent.Init -> {
                reduce {
                    it.copy(
                        address = route.address,
                        deliveryDate = route.deliveryDate
                    )
                }
            }
            is FittingInfoIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FittingInfoRoute): FittingInfoViewModel
    }
}
