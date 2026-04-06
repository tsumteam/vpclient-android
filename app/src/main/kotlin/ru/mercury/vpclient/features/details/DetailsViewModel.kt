package ru.mercury.vpclient.features.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.features.details.intent.DetailsIntent
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute

// fixme

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val route: DetailsRoute,
    private val interactor: Interactor
): ClientViewModel<DetailsIntent, DetailsModel, Event>(DetailsModel()) {

    init {
        dispatch(DetailsIntent.CollectProduct)
        dispatch(DetailsIntent.LoadProduct)
    }

    override fun dispatch(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.CollectProduct -> launch {
                interactor.productFlow(route.id).collectLatest { productEntity ->
                    reduce { it.copy(productEntity = productEntity) }
                }
            }
            is DetailsIntent.LoadProduct -> launch { interactor.loadProduct(route.id) }
            is DetailsIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is DetailsIntent.MessageClick -> Unit
            is DetailsIntent.SizeTableClick -> Unit
            is DetailsIntent.SizeClick -> {
                val size = stateFlow.value.productEntity.availableSizes?.items?.getOrNull(intent.index)
                reduce { it.copy(selectedSizeId = size?.sizeId) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: DetailsRoute): DetailsViewModel
    }
}
