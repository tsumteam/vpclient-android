package ru.mercury.vpclient.features.fitting_addresses

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEventManager
import ru.mercury.vpclient.features.fitting_addresses.intent.FittingAddressesIntent
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesRoute
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddress
import ru.mercury.vpclient.shared.domain.mapper.fittingAddressModel
import ru.mercury.vpclient.shared.domain.mapper.updated
import ru.mercury.vpclient.shared.domain.mapper.withSuggestion
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressListUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressListUseCase.ClientAddressListException
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.DeleteClientDeliveryAddressUseCase.ClientAddressException
import ru.mercury.vpclient.shared.domain.usecase.DeleteClientDeliveryAddressUseCase
import ru.mercury.vpclient.shared.domain.usecase.SaveClientDeliveryAddressUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FittingAddressesViewModel.Factory::class)
class FittingAddressesViewModel @AssistedInject constructor(
    @Assisted private val route: FittingAddressesRoute,
    private val clientAddressesFlowUseCase: ClientAddressesFlowUseCase,
    private val clientAddressListUseCase: ClientAddressListUseCase,
    private val saveClientDeliveryAddressUseCase: SaveClientDeliveryAddressUseCase,
    private val deleteClientDeliveryAddressUseCase: DeleteClientDeliveryAddressUseCase
): ClientViewModel<FittingAddressesIntent, FittingConfirmationModel, FittingConfirmationEvent>(FittingConfirmationModel()) {

    init {
        dispatch(FittingAddressesIntent.CollectRoute)
        dispatch(FittingAddressesIntent.CollectClientAddresses)
        dispatch(FittingAddressesIntent.LoadClientAddresses)
    }

    override fun dispatch(intent: FittingAddressesIntent) {
        when (intent) {
            is FittingAddressesIntent.CollectRoute -> {
                reduce {
                    it.copy(
                        productIds = route.confirmationRoute.productIds,
                        deliveryId = route.confirmationRoute.deliveryId,
                        fittingType = route.confirmationRoute.fittingType,
                        selectedClientAddressId = route.selectedClientAddressId,
                        pendingClientAddressId = route.selectedClientAddressId,
                        clientAddress = route.clientAddress
                    )
                }
            }
            is FittingAddressesIntent.CollectClientAddresses -> {
                launch {
                    clientAddressesFlowUseCase(Unit).collectLatest { entities ->
                        reduce { state ->
                            val selectedId = state.selectedClientAddressId
                                ?.takeIf { id -> entities.any { address -> address.id == id } }
                                ?: entities.firstOrNull { address -> address.address == state.clientAddress }?.id
                                ?: entities.firstOrNull()?.id

                            state.copy(
                                clientAddresses = entities,
                                selectedClientAddressId = selectedId,
                                pendingClientAddressId = state.pendingClientAddressId
                                    ?.takeIf { id -> entities.any { address -> address.id == id } }
                                    ?: selectedId
                            )
                        }
                    }
                }
            }
            is FittingAddressesIntent.LoadClientAddresses -> {
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
            is FittingAddressesIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is FittingAddressesIntent.SaveAddressSelectionClick -> {
                launch {
                    val state = stateFlow.value
                    val selectedAddressId = state.pendingClientAddressId
                    if (selectedAddressId != null) {
                        FittingAddressesEventManager.send(
                            FittingAddressesEvent.SelectAddress(
                                confirmationRoute = route.confirmationRoute,
                                selectedClientAddressId = selectedAddressId,
                                clientAddresses = state.clientAddresses
                            )
                        )
                    }
                    MainEventManager.send(BackRoute)
                }
            }
            is FittingAddressesIntent.SelectClientAddress -> reduce {
                it.copy(pendingClientAddressId = intent.addressId)
            }
            is FittingAddressesIntent.AddAddressClick -> {
                reduce {
                    it.copy(
                        isAddressFormVisible = true,
                        addressForm = FittingAddressModel()
                    )
                }
            }
            is FittingAddressesIntent.HideAddressForm -> {
                reduce {
                    it.copy(
                        isAddressFormVisible = false,
                        isAddressSaving = false
                    )
                }
            }
            is FittingAddressesIntent.OpenAddressSearch -> reduce {
                it.copy(isAddressSearchVisible = true)
            }
            is FittingAddressesIntent.HideAddressSearch -> {
                reduce { it.copy(isAddressSearchVisible = false) }
            }
            is FittingAddressesIntent.AddressFormValueChange -> reduce {
                it.copy(addressForm = it.addressForm.updated(intent.field, intent.value))
            }
            is FittingAddressesIntent.SelectAddressSuggestion -> {
                reduce {
                    it.copy(
                        addressForm = it.addressForm.withSuggestion(intent.suggestion),
                        isAddressSearchVisible = false
                    )
                }
            }
            is FittingAddressesIntent.SaveAddressClick -> {
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
                                    selectedClientAddressId = when {
                                        !form.isEdit -> savedAddress.id
                                        else -> state.selectedClientAddressId
                                    },
                                    pendingClientAddressId = when {
                                        !form.isEdit -> savedAddress.id
                                        else -> state.pendingClientAddressId
                                    },
                                    isAddressFormVisible = false,
                                    isAddressSaving = false
                                )
                            }
                            if (!form.isEdit) {
                                FittingAddressesEventManager.send(
                                    FittingAddressesEvent.SelectAddress(
                                        confirmationRoute = route.confirmationRoute,
                                        selectedClientAddressId = stateFlow.value.pendingClientAddressId,
                                        clientAddresses = stateFlow.value.clientAddresses
                                    )
                                )
                                MainEventManager.send(BackRoute)
                            }
                        }
                        .onFailure { error ->
                            reduce { it.copy(isAddressSaving = false) }
                            throw error
                        }
                }
            }
            is FittingAddressesIntent.OpenAddressActions -> reduce {
                it.copy(addressActionAddressId = intent.addressId)
            }
            is FittingAddressesIntent.HideAddressActions -> reduce {
                it.copy(addressActionAddressId = null)
            }
            is FittingAddressesIntent.EditAddressClick -> {
                reduce {
                    it.copy(
                        addressForm = it.addressActionAddress?.fittingAddressModel ?: FittingAddressModel(),
                        isAddressFormVisible = true,
                        addressActionAddressId = null
                    )
                }
            }
            is FittingAddressesIntent.RequestDeleteAddress -> {
                reduce { it.copy(addressActionAddressId = null, deleteAddressId = intent.addressId) }
            }
            is FittingAddressesIntent.DismissDeleteAddress -> {
                reduce { it.copy(deleteAddressId = null) }
            }
            is FittingAddressesIntent.ConfirmDeleteAddress -> {
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
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ClientAddressListException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientAddressException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is RoomException, is RoomSQLiteException -> {
                launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FittingAddressesRoute): FittingAddressesViewModel
    }
}
