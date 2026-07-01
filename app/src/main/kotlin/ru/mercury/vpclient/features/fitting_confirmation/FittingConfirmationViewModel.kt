package ru.mercury.vpclient.features.fitting_confirmation

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesRoute
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessDeliveryLine
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessRoute
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddress
import ru.mercury.vpclient.shared.domain.mapper.fittingAddressModel
import ru.mercury.vpclient.shared.domain.mapper.fittingConfirmationPlaceType
import ru.mercury.vpclient.shared.domain.mapper.fittingTypeDto
import ru.mercury.vpclient.shared.domain.mapper.selectedDayId
import ru.mercury.vpclient.shared.domain.mapper.selectedDeliveryDayIds
import ru.mercury.vpclient.shared.domain.mapper.selectedDeliveryIntervalIds
import ru.mercury.vpclient.shared.domain.mapper.selectedIntervalId
import ru.mercury.vpclient.shared.domain.mapper.updated
import ru.mercury.vpclient.shared.domain.mapper.updatedFor
import ru.mercury.vpclient.shared.domain.mapper.withSuggestion
import ru.mercury.vpclient.shared.domain.usecase.CartProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressListUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressListUseCase.ClientAddressListException
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ConfirmFittingUseCase
import ru.mercury.vpclient.shared.domain.usecase.ConfirmFittingUseCase.ConfirmFittingException
import ru.mercury.vpclient.shared.domain.usecase.DeleteClientDeliveryAddressUseCase.ClientAddressException
import ru.mercury.vpclient.shared.domain.usecase.DeleteClientDeliveryAddressUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingConfirmationDataUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingUseCase
import ru.mercury.vpclient.shared.domain.usecase.SaveClientDeliveryAddressUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FittingConfirmationViewModel.Factory::class)
class FittingConfirmationViewModel @AssistedInject constructor(
    @Assisted private val route: FittingConfirmationRoute,
    private val cartProductsFlowUseCase: CartProductsFlowUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val loadFittingConfirmationDataUseCase: LoadFittingConfirmationDataUseCase,
    private val confirmFittingUseCase: ConfirmFittingUseCase,
    private val clientAddressesFlowUseCase: ClientAddressesFlowUseCase,
    private val clientAddressListUseCase: ClientAddressListUseCase,
    private val saveClientDeliveryAddressUseCase: SaveClientDeliveryAddressUseCase,
    private val deleteClientDeliveryAddressUseCase: DeleteClientDeliveryAddressUseCase
): ClientViewModel<FittingConfirmationIntent, FittingConfirmationModel, FittingConfirmationEvent>(FittingConfirmationModel()) {

    init {
        dispatch(FittingConfirmationIntent.CollectRoute)
        dispatch(FittingConfirmationIntent.CollectCartProducts)
        dispatch(FittingConfirmationIntent.CollectClientAddresses)
        dispatch(FittingConfirmationIntent.LoadFittingData)
        dispatch(FittingConfirmationIntent.LoadClientAddresses)
    }

    override fun dispatch(intent: FittingConfirmationIntent) {
        when (intent) {
            is FittingConfirmationIntent.CollectRoute -> {
                reduce {
                    it.copy(
                        productIds = route.productIds,
                        deliveryId = route.deliveryId,
                        fittingType = route.fittingType,
                        selectedPlaceType = route.fittingType?.fittingConfirmationPlaceType ?: it.selectedPlaceType
                    )
                }
            }
            is FittingConfirmationIntent.CollectCartProducts -> {
                launch {
                    cartProductsFlowUseCase(Unit).collectLatest { products ->
                        reduce { state -> state.copy(products = (state.products + products).distinctBy { it.id }) }
                    }
                }
            }
            is FittingConfirmationIntent.CollectClientAddresses -> {
                launch {
                    clientAddressesFlowUseCase(Unit).collectLatest { addresses ->
                        reduce { state ->
                            val selectedId = state.selectedClientAddressId
                                ?.takeIf { id -> addresses.any { address -> address.id == id } }
                                ?: addresses.firstOrNull { address -> address.address == state.clientAddress }?.id
                                ?: addresses.firstOrNull()?.id

                            state.copy(
                                clientAddresses = addresses,
                                selectedClientAddressId = selectedId,
                                pendingClientAddressId = state.pendingClientAddressId
                                    ?.takeIf { id -> addresses.any { address -> address.id == id } }
                                    ?: selectedId
                            )
                        }
                    }
                }
            }
            is FittingConfirmationIntent.ReceiveFittingAddressesEvent -> {
                when (val event = intent.event) {
                    is FittingAddressesEvent.SelectAddress -> {
                        val state = stateFlow.value
                        if (
                            event.confirmationRoute.productIds == state.productIds &&
                            event.confirmationRoute.deliveryId == state.deliveryId &&
                            event.confirmationRoute.fittingType == state.fittingType
                        ) {
                            reduce {
                                it.copy(
                                    clientAddresses = event.clientAddresses,
                                    selectedClientAddressId = event.selectedClientAddressId,
                                    pendingClientAddressId = event.selectedClientAddressId,
                                    selectedPlaceType = FittingConfirmationPlaceType.Home
                                )
                            }
                            dispatch(FittingConfirmationIntent.LoadFittingData)
                        }
                    }
                }
            }
            is FittingConfirmationIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is FittingConfirmationIntent.ConfirmClick -> {
                launch {
                    reduce { it.copy(isConfirmLoading = true) }
                    val state = stateFlow.value
                    runCatching {
                        confirmFittingUseCase(
                            ConfirmFittingUseCase.Params(
                                products = state.selectedProducts,
                                fittingType = state.selectedPlaceType.fittingTypeDto,
                                clientAddress = state.selectedClientAddress,
                                singleInterval = state.selectedSingleInterval,
                                deliveryGroups = state.deliveryGroups,
                                selectedDeliveryIntervalIds = state.selectedDeliveryIntervalIds,
                                useSingleDelivery = state.deliveryMode == FittingConfirmationDeliveryMode.Single
                            )
                        ).getOrThrow()
                    }
                        .onSuccess { result ->
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
                        .onFailure { error ->
                            reduce { it.copy(isConfirmLoading = false) }
                            throw error
                        }
                }
            }
            is FittingConfirmationIntent.LoadFittingData -> {
                launch {
                    reduce { it.copy(isIntervalsLoading = true, intervalsError = null) }

                    val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrNull()
                    val products = (stateFlow.value.products + fitting?.products.orEmpty()).distinctBy { it.id }
                    val selectedProducts = stateFlow.value.productIds.mapNotNull { productId ->
                        products.firstOrNull { product -> product.id == productId }
                    }

                    when {
                        selectedProducts.isEmpty() -> {
                            reduce {
                                it.copy(
                                    products = products,
                                    isInitialIntervalsLoading = false,
                                    isIntervalsLoading = false
                                )
                            }
                        }
                        else -> {
                            val fittingType = stateFlow.value.selectedPlaceType.fittingTypeDto
                            val result = runCatching {
                                loadFittingConfirmationDataUseCase(
                                    LoadFittingConfirmationDataUseCase.Params(
                                        products = selectedProducts,
                                        deliveryId = stateFlow.value.deliveryId,
                                        fittingType = fittingType,
                                        clientAddress = stateFlow.value.selectedClientAddress
                                    )
                                ).getOrThrow()
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
                                            selectedSingleDayId = data.singleIntervals.selectedDayId(
                                                it.selectedSingleDayId
                                            ),
                                            selectedSingleIntervalId = data.singleIntervals.selectedIntervalId(
                                                it.selectedSingleIntervalId
                                            ),
                                            selectedDeliveryDayIds = data.deliveryGroups.selectedDeliveryDayIds(
                                                it.selectedDeliveryDayIds
                                            ),
                                            selectedDeliveryIntervalIds = data.deliveryGroups.selectedDeliveryIntervalIds(
                                                it.selectedDeliveryIntervalIds
                                            ),
                                            selectedClientAddressId = it.selectedClientAddressId
                                                ?: it.clientAddresses.firstOrNull { address ->
                                                    address.address == data.clientAddress
                                                }?.id
                                                ?: it.clientAddresses.firstOrNull()?.id,
                                            pendingClientAddressId = it.pendingClientAddressId
                                                ?: it.selectedClientAddressId
                                                ?: it.clientAddresses.firstOrNull { address ->
                                                    address.address == data.clientAddress
                                                }?.id
                                                ?: it.clientAddresses.firstOrNull()?.id,
                                            expandedDeliveryId = it.expandedDeliveryId?.takeIf { id ->
                                                data.deliveryGroups.any { group -> group.id == id }
                                            },
                                            isInitialIntervalsLoading = false,
                                            isIntervalsLoading = false,
                                            intervalsError = null
                                        )
                                    }
                                }
                                .onFailure { error ->
                                    reduce {
                                        it.copy(
                                            products = products,
                                            isInitialIntervalsLoading = false,
                                            isIntervalsLoading = false,
                                            intervalsError = error.message
                                        )
                                    }
                                }
                        }
                    }
                }
            }
            is FittingConfirmationIntent.LoadClientAddresses -> {
                stateFlow.value.addressListJob?.cancel()
                val job = launch {
                    clientAddressListUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state ->
                            when (state.addressListJob) {
                                launchedJob -> state.copy(addressListJob = null)
                                else -> state
                            }
                        }
                    }
                }
                reduce { it.copy(addressListJob = job) }
            }
            is FittingConfirmationIntent.OpenAddressSelection -> {
                reduce { it.copy(pendingClientAddressId = it.selectedClientAddressId) }
                launch {
                    val state = stateFlow.value
                    MainEventManager.send(
                        FittingAddressesRoute(
                            confirmationRoute = FittingConfirmationRoute(
                                productIds = state.productIds,
                                deliveryId = state.deliveryId,
                                fittingType = state.fittingType
                            ),
                            selectedClientAddressId = state.selectedClientAddressId,
                            clientAddress = state.clientAddress
                        )
                    )
                }
                dispatch(FittingConfirmationIntent.LoadClientAddresses)
            }
            is FittingConfirmationIntent.SelectClientAddress -> {
                reduce { it.copy(pendingClientAddressId = intent.addressId) }
            }
            is FittingConfirmationIntent.AddAddressClick -> {
                reduce {
                    it.copy(
                        isAddressFormVisible = true,
                        addressForm = FittingAddressModel()
                    )
                }
            }
            is FittingConfirmationIntent.HideAddressForm -> {
                reduce { it.copy(isAddressFormVisible = false, isAddressSaving = false) }
            }
            is FittingConfirmationIntent.OpenAddressSearch -> reduce {
                it.copy(isAddressSearchVisible = true)
            }
            is FittingConfirmationIntent.HideAddressSearch -> {
                reduce { it.copy(isAddressSearchVisible = false) }
            }
            is FittingConfirmationIntent.AddressFormValueChange -> reduce {
                it.copy(addressForm = it.addressForm.updated(intent.field, intent.value))
            }
            is FittingConfirmationIntent.SelectAddressSuggestion -> {
                reduce {
                    it.copy(
                        addressForm = it.addressForm.withSuggestion(intent.suggestion),
                        isAddressSearchVisible = false
                    )
                }
            }
            is FittingConfirmationIntent.SaveAddressClick -> {
                launch {
                    reduce { it.copy(isAddressSaving = true) }
                    val form = stateFlow.value.addressForm
                    val address = form.clientDeliveryAddress()
                    runCatching {
                        saveClientDeliveryAddressUseCase(
                            SaveClientDeliveryAddressUseCase.Params(
                                address = address,
                                isEdit = form.isEdit
                            )
                        ).getOrThrow()
                    }
                        .onSuccess { savedAddress ->
                            reduce { state ->
                                val addresses = (state.clientAddresses.filter { address ->
                                    address.id != savedAddress.id
                                } + savedAddress).sortedBy { address -> address.id }

                                state.copy(
                                    clientAddresses = addresses,
                                    selectedClientAddressId = savedAddress.id,
                                    pendingClientAddressId = savedAddress.id,
                                    selectedPlaceType = FittingConfirmationPlaceType.Home,
                                    isAddressFormVisible = false,
                                    isAddressSaving = false
                                )
                            }
                            dispatch(FittingConfirmationIntent.LoadFittingData)
                        }
                        .onFailure { error ->
                            reduce { it.copy(isAddressSaving = false) }
                            throw error
                        }
                }
            }
            is FittingConfirmationIntent.OpenAddressActions -> reduce { it.copy(addressActionAddressId = intent.addressId) }
            is FittingConfirmationIntent.HideAddressActions -> reduce { it.copy(addressActionAddressId = null) }
            is FittingConfirmationIntent.EditAddressClick -> {
                reduce {
                    it.copy(
                        addressForm = it.addressActionAddress?.fittingAddressModel ?: FittingAddressModel(),
                        isAddressFormVisible = true,
                        addressActionAddressId = null
                    )
                }
            }
            is FittingConfirmationIntent.RequestDeleteAddress -> reduce { it.copy(addressActionAddressId = null, deleteAddressId = intent.addressId) }
            is FittingConfirmationIntent.DismissDeleteAddress -> reduce { it.copy(deleteAddressId = null) }
            is FittingConfirmationIntent.ConfirmDeleteAddress -> {
                launch {
                    val addressId = stateFlow.value.deleteAddressId ?: return@launch
                    deleteClientDeliveryAddressUseCase(addressId).getOrThrow()
                    reduce { state ->
                        val addresses = state.clientAddresses.filter { address -> address.id != addressId }
                        val selectedId = when (state.selectedClientAddressId) {
                            addressId -> addresses.firstOrNull()?.id
                            else -> state.selectedClientAddressId
                        }

                        state.copy(
                            clientAddresses = addresses,
                            selectedClientAddressId = selectedId,
                            pendingClientAddressId = when (state.pendingClientAddressId) {
                                addressId -> selectedId
                                else -> state.pendingClientAddressId
                            },
                            deleteAddressId = null
                        )
                    }
                    dispatch(FittingConfirmationIntent.LoadFittingData)
                }
            }
            is FittingConfirmationIntent.SelectPlace -> {
                reduce { it.copy(selectedPlaceType = intent.placeType) }
                dispatch(FittingConfirmationIntent.LoadFittingData)
            }
            is FittingConfirmationIntent.SelectDeliveryMode -> reduce { it.copy(deliveryMode = intent.mode) }
            is FittingConfirmationIntent.SelectSingleDay -> reduce {
                it.copy(
                    selectedSingleDayId = intent.dayId,
                    selectedSingleIntervalId = it.singleIntervals.firstOrNull { interval -> interval.dayId == intent.dayId }?.id
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

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ConfirmFittingException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientAddressListException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientAddressException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FittingConfirmationRoute): FittingConfirmationViewModel
    }
}
