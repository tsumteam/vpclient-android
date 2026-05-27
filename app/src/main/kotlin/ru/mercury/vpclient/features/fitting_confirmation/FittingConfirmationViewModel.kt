package ru.mercury.vpclient.features.fitting_confirmation

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationPlaceType
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessDeliveryLine
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessRoute
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.error.ConfirmFittingException
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FittingConfirmationViewModel.Factory::class)
class FittingConfirmationViewModel @AssistedInject constructor(
    @Assisted route: FittingConfirmationRoute,
    private val cartInteractor: CartInteractor
): ClientViewModel<FittingConfirmationIntent, FittingConfirmationModel, FittingConfirmationEvent>(FittingConfirmationModel(route)) {

    init {
        route.fittingType?.let { fittingType ->
            reduce {
                it.copy(selectedPlaceType = fittingType.fittingConfirmationPlaceType)
            }
        }
        launch {
            cartInteractor.cartProductsFlow.collectLatest { products ->
                reduce { state -> state.copy(products = (state.products + products).distinctBy { it.id }) }
            }
        }
        dispatch(FittingConfirmationIntent.LoadFittingData)
    }

    override fun dispatch(intent: FittingConfirmationIntent) {
        when (intent) {
            is FittingConfirmationIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is FittingConfirmationIntent.ConfirmClick -> confirmFitting()
            is FittingConfirmationIntent.LoadFittingData -> loadFittingData()
            is FittingConfirmationIntent.SelectPlace -> {
                reduce { it.copy(selectedPlaceType = intent.placeType) }
                dispatch(FittingConfirmationIntent.LoadFittingData)
            }
            is FittingConfirmationIntent.SelectDeliveryMode -> reduce { it.copy(deliveryMode = intent.mode) }
            is FittingConfirmationIntent.SelectSingleDay -> reduce {
                it.copy(
                    selectedSingleDayId = intent.dayId,
                    selectedSingleIntervalId = it.singleIntervals.firstOrNull { interval ->
                        interval.dayId == intent.dayId
                    }?.id
                )
            }
            is FittingConfirmationIntent.SelectSingleInterval -> reduce {
                it.copy(selectedSingleIntervalId = intent.intervalId)
            }
            is FittingConfirmationIntent.SelectDeliveryDay -> reduce {
                val intervalId = it.deliveryGroups
                    .firstOrNull { group -> group.id == intent.deliveryId }
                    ?.intervals
                    ?.firstOrNull { interval -> interval.dayId == intent.dayId }
                    ?.id
                it.copy(
                    selectedDeliveryDayIds = it.selectedDeliveryDayIds + (intent.deliveryId to intent.dayId),
                    selectedDeliveryIntervalIds = when {
                        intervalId != null -> it.selectedDeliveryIntervalIds + (intent.deliveryId to intervalId)
                        else -> it.selectedDeliveryIntervalIds - intent.deliveryId
                    }
                )
            }
            is FittingConfirmationIntent.SelectDeliveryInterval -> reduce {
                it.copy(
                    selectedDeliveryIntervalIds = it.selectedDeliveryIntervalIds + (intent.deliveryId to intent.intervalId)
                )
            }
            is FittingConfirmationIntent.ChangeDeliveryTimeClick -> reduce {
                it.copy(
                    expandedDeliveryId = when (it.expandedDeliveryId) {
                        intent.deliveryId -> null
                        else -> intent.deliveryId
                    }
                )
            }
        }
    }

    private fun loadFittingData() {
        launch {
            reduce { it.copy(isIntervalsLoading = true, intervalsError = null) }

            val fitting = runCatching { cartInteractor.loadFitting() }.getOrNull()
            val products = (stateFlow.value.products + fitting?.products.orEmpty()).distinctBy { it.id }
            val selectedProducts = stateFlow.value.route.productIds.mapNotNull { productId ->
                products.firstOrNull { product -> product.id == productId }
            }

            when {
                selectedProducts.isEmpty() -> {
                    reduce {
                        it.copy(
                            products = products,
                            isIntervalsLoading = false
                        )
                    }
                }
                else -> {
                    val route = stateFlow.value.route
                    val fittingType = stateFlow.value.selectedPlaceType.fittingTypeDto
                    val result = runCatching {
                        when (val deliveryId = route.deliveryId) {
                            null -> {
                                cartInteractor.loadFittingConfirmationData(
                                    products = selectedProducts,
                                    fittingType = fittingType
                                )
                            }
                            else -> {
                                cartInteractor.loadExistingFittingConfirmationData(
                                    products = selectedProducts,
                                    deliveryId = deliveryId,
                                    fittingType = fittingType
                                )
                            }
                        }
                    }
                    result
                        .onSuccess { data ->
                            reduce {
                                it.copy(
                                    products = products,
                                    boutiqueAddress = data.boutiqueAddress,
                                    clientAddress = data.clientAddress,
                                    isClientAddressAvailable = data.isClientAddressAvailable,
                                    deliveryMode = it.deliveryMode.updatedFor(data.deliveryGroups),
                                    singleIntervals = data.singleIntervals,
                                    deliveryGroups = data.deliveryGroups,
                                    selectedSingleDayId = data.singleIntervals.selectedDayId(it.selectedSingleDayId),
                                    selectedSingleIntervalId = data.singleIntervals.selectedIntervalId(it.selectedSingleIntervalId),
                                    selectedDeliveryDayIds = data.deliveryGroups.selectedDeliveryDayIds(it.selectedDeliveryDayIds),
                                    selectedDeliveryIntervalIds = data.deliveryGroups.selectedDeliveryIntervalIds(
                                        it.selectedDeliveryIntervalIds
                                    ),
                                    expandedDeliveryId = it.expandedDeliveryId?.takeIf { id ->
                                        data.deliveryGroups.any { group -> group.id == id }
                                    },
                                    isIntervalsLoading = false,
                                    intervalsError = null
                                )
                            }
                        }
                        .onFailure { error ->
                            reduce {
                                it.copy(
                                    products = products,
                                    isIntervalsLoading = false,
                                    intervalsError = error.message
                                )
                            }
                        }
                }
            }
        }
    }

    private fun confirmFitting() {
        launch {
            reduce { it.copy(isConfirmLoading = true) }
            val state = stateFlow.value
            val result = cartInteractor.confirmFitting(
                products = state.selectedProducts,
                fittingType = state.selectedPlaceType.fittingTypeDto,
                singleInterval = state.selectedSingleInterval,
                deliveryGroups = state.deliveryGroups,
                selectedDeliveryIntervalIds = state.selectedDeliveryIntervalIds,
                useSingleDelivery = state.deliveryMode == FittingConfirmationDeliveryMode.Single
            )
            reduce { it.copy(isConfirmLoading = false) }
            MainEventManager.send(
                FittingSuccessRoute(
                    deliveryLines = result.deliveryLines.map { line ->
                        FittingSuccessDeliveryLine(
                            intervalSummary = line.intervalSummary,
                            productsCount = line.productsCount
                        )
                    },
                    address = result.address
                )
            )
        }
    }

    override fun catch(throwable: Throwable) {
        reduce { it.copy(isConfirmLoading = false) }
        when (throwable) {
            is ConfirmFittingException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FittingConfirmationRoute): FittingConfirmationViewModel
    }
}

private val FittingConfirmationPlaceType.fittingTypeDto: FittingTypeDtoEnum
    get() {
        return when (this) {
            FittingConfirmationPlaceType.Boutique -> FittingTypeDtoEnum.IN_THE_STORE
            FittingConfirmationPlaceType.Home -> FittingTypeDtoEnum.AT_HOME
            FittingConfirmationPlaceType.Other -> FittingTypeDtoEnum.AT_HOME
        }
    }

private val FittingTypeDtoEnum.fittingConfirmationPlaceType: FittingConfirmationPlaceType
    get() {
        return when (this) {
            FittingTypeDtoEnum.IN_THE_STORE -> FittingConfirmationPlaceType.Boutique
            FittingTypeDtoEnum.AT_HOME -> FittingConfirmationPlaceType.Home
        }
    }

private fun FittingConfirmationDeliveryMode.updatedFor(
    groups: List<FittingConfirmationDeliveryGroup>
): FittingConfirmationDeliveryMode {
    return when {
        groups.size > 1 -> this
        else -> FittingConfirmationDeliveryMode.Single
    }
}

private fun List<FittingConfirmationDeliveryInterval>.selectedDayId(current: String?): String? {
    return current?.takeIf { dayId -> any { interval -> interval.dayId == dayId } }
        ?: firstOrNull()?.dayId
}

private fun List<FittingConfirmationDeliveryInterval>.selectedIntervalId(current: String?): String? {
    return current?.takeIf { intervalId -> any { interval -> interval.id == intervalId } }
        ?: firstOrNull()?.id
}

private fun List<FittingConfirmationDeliveryGroup>.selectedDeliveryDayIds(
    current: Map<String, String>
): Map<String, String> {
    return associate { group ->
        val selectedDayId = group.intervals.selectedDayId(current[group.id]).orEmpty()
        group.id to selectedDayId
    }.filterValues { it.isNotEmpty() }
}

private fun List<FittingConfirmationDeliveryGroup>.selectedDeliveryIntervalIds(
    current: Map<String, String>
): Map<String, String> {
    return associate { group ->
        val selectedIntervalId = group.intervals.selectedIntervalId(current[group.id]).orEmpty()
        group.id to selectedIntervalId
    }.filterValues { it.isNotEmpty() }
}
