package ru.mercury.vpclient.features.fitting_addresses

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_address_actions_sheet.intent.FittingAddressActionsIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.intent.FittingAddressDeleteIntent
import ru.mercury.vpclient.features.fitting_address_sheet.intent.FittingAddressIntent
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEventManager
import ru.mercury.vpclient.features.fitting_addresses.intent.FittingAddressesIntent
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesOrigin
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
import ru.mercury.vpclient.shared.domain.usecase.DeleteClientDeliveryAddressUseCase
import ru.mercury.vpclient.shared.domain.usecase.DeleteClientDeliveryAddressUseCase.ClientAddressException
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
                val fittingOrigin = route.origin as? FittingAddressesOrigin.Fitting
                reduce {
                    it.copy(
                        productIds = fittingOrigin?.confirmationRoute?.productIds.orEmpty(),
                        deliveryId = fittingOrigin?.confirmationRoute?.deliveryId,
                        fittingType = fittingOrigin?.confirmationRoute?.fittingType,
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
                                origin = route.origin,
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
                        isFittingAddressSheetVisible = true,
                        fittingAddressState = FittingAddressModel()
                    )
                }
            }
            is FittingAddressesIntent.OnFittingAddressIntent -> {
                when (intent.intent) {
                    is FittingAddressIntent.DismissClick -> {
                        reduce {
                            it.copy(
                                isFittingAddressSheetVisible = false,
                                isAddressSaving = false
                            )
                        }
                    }
                    is FittingAddressIntent.OpenAddressSearch -> reduce {
                        it.copy(isFittingAddressSearchSheetVisible = true)
                    }
                    is FittingAddressIntent.AddressFormValueChange -> reduce {
                        it.copy(fittingAddressState = it.fittingAddressState.updated(intent.intent.field, intent.intent.value))
                    }
                    is FittingAddressIntent.SaveAddressClick -> {
                        launch {
                            reduce { it.copy(isAddressSaving = true) }
                            val form = stateFlow.value.fittingAddressState
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
                                            isFittingAddressSheetVisible = false,
                                            isAddressSaving = false
                                        )
                                    }
                                    if (!form.isEdit) {
                                        FittingAddressesEventManager.send(
                                            FittingAddressesEvent.SelectAddress(
                                                origin = route.origin,
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
                }
            }
            is FittingAddressesIntent.DismissFittingAddressSearchSheet -> {
                reduce { it.copy(isFittingAddressSearchSheetVisible = false) }
            }
            is FittingAddressesIntent.SelectAddressSuggestion -> {
                reduce {
                    it.copy(
                        fittingAddressState = it.fittingAddressState.withSuggestion(intent.suggestion),
                        isFittingAddressSearchSheetVisible = false
                    )
                }
            }
            is FittingAddressesIntent.OpenAddressActions -> reduce {
                it.copy(addressActionAddressId = intent.addressId)
            }
            is FittingAddressesIntent.OnFittingAddressActionsIntent -> {
                when (intent.intent) {
                    is FittingAddressActionsIntent.DismissClick -> reduce {
                        it.copy(addressActionAddressId = null)
                    }
                    is FittingAddressActionsIntent.EditClick -> {
                        reduce {
                            it.copy(
                                fittingAddressState = it.addressActionAddress?.fittingAddressModel ?: FittingAddressModel(),
                                isFittingAddressSheetVisible = true,
                                addressActionAddressId = null
                            )
                        }
                    }
                    is FittingAddressActionsIntent.DeleteClick -> {
                        val addressId = stateFlow.value.addressActionAddressId ?: return
                        reduce { it.copy(addressActionAddressId = null, deleteAddressId = addressId) }
                    }
                }
            }
            is FittingAddressesIntent.OnFittingAddressDeleteIntent -> {
                when (intent.intent) {
                    is FittingAddressDeleteIntent.DismissRequest -> {
                        reduce { it.copy(deleteAddressId = null) }
                    }
                    is FittingAddressDeleteIntent.ConfirmClick -> {
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
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ClientAddressListException -> {
                launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            }
            is ClientAddressException -> {
                launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            }
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
