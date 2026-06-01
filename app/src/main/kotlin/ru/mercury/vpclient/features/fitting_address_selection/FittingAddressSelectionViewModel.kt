package ru.mercury.vpclient.features.fitting_address_selection

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.fitting_address_selection.event.FittingAddressSelectionResult
import ru.mercury.vpclient.features.fitting_address_selection.event.FittingAddressSelectionResultManager
import ru.mercury.vpclient.features.fitting_address_selection.navigation.FittingAddressSelectionRoute
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField
import ru.mercury.vpclient.shared.data.error.ClientAddressException
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddress
import ru.mercury.vpclient.shared.domain.mapper.fittingAddressModel
import ru.mercury.vpclient.shared.domain.mapper.updated
import ru.mercury.vpclient.shared.domain.mapper.withSuggestion
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FittingAddressSelectionViewModel.Factory::class)
class FittingAddressSelectionViewModel @AssistedInject constructor(
    @Assisted private val route: FittingAddressSelectionRoute,
    private val cartInteractor: CartInteractor
): ClientViewModel<FittingConfirmationIntent, FittingConfirmationModel, FittingConfirmationEvent>(
    FittingConfirmationModel(
        route = route.confirmationRoute,
        selectedClientAddressId = route.selectedClientAddressId,
        pendingClientAddressId = route.selectedClientAddressId,
        clientAddress = route.clientAddress
    )
) {

    init {
        dispatch(FittingConfirmationIntent.LoadClientAddresses)
    }

    override fun dispatch(intent: FittingConfirmationIntent) {
        when (intent) {
            is FittingConfirmationIntent.LoadClientAddresses -> loadClientAddresses()
            is FittingConfirmationIntent.AddressSelectionBackClick -> launch { MainEventManager.send(BackRoute) }
            is FittingConfirmationIntent.SaveAddressSelectionClick -> saveSelection()
            is FittingConfirmationIntent.SelectClientAddress -> reduce {
                it.copy(pendingClientAddressId = intent.addressId)
            }
            is FittingConfirmationIntent.AddAddressClick -> reduce {
                it.copy(
                    isAddressFormVisible = true,
                    addressForm = FittingAddressModel(),
                    addressSuggestions = emptyList(),
                    isAddressSuggestionsLoading = false,
                    addressSearchQuery = ""
                )
            }
            is FittingConfirmationIntent.HideAddressForm -> reduce {
                it.copy(isAddressFormVisible = false, isAddressSaving = false)
            }
            is FittingConfirmationIntent.OpenAddressSearch -> reduce {
                it.copy(
                    isAddressSearchVisible = true,
                    addressSearchQuery = it.addressForm.address
                )
            }
            is FittingConfirmationIntent.HideAddressSearch -> reduce {
                it.copy(isAddressSearchVisible = false, isAddressSuggestionsLoading = false)
            }
            is FittingConfirmationIntent.AddressFormValueChange -> reduce {
                it.copy(addressForm = it.addressForm.updated(intent.field, intent.value))
            }
            is FittingConfirmationIntent.AddressSearchQueryChange -> {
                reduce { it.copy(addressSearchQuery = intent.query) }
                searchAddress(intent.query)
            }
            is FittingConfirmationIntent.SelectAddressSuggestion -> reduce {
                it.copy(
                    addressForm = it.addressForm.withSuggestion(intent.suggestion),
                    isAddressSearchVisible = false
                )
            }
            is FittingConfirmationIntent.SaveAddressClick -> saveAddress()
            is FittingConfirmationIntent.OpenAddressActions -> reduce {
                it.copy(addressActionAddressId = intent.addressId)
            }
            is FittingConfirmationIntent.HideAddressActions -> reduce {
                it.copy(addressActionAddressId = null)
            }
            is FittingConfirmationIntent.EditAddressClick -> reduce {
                it.copy(
                    addressForm = it.addressActionAddress?.fittingAddressModel ?: FittingAddressModel(),
                    isAddressFormVisible = true,
                    addressActionAddressId = null,
                    addressSuggestions = emptyList(),
                    isAddressSuggestionsLoading = false,
                    addressSearchQuery = ""
                )
            }
            is FittingConfirmationIntent.RequestDeleteAddress -> reduce {
                it.copy(addressActionAddressId = null, deleteAddressId = intent.addressId)
            }
            is FittingConfirmationIntent.DismissDeleteAddress -> reduce {
                it.copy(deleteAddressId = null)
            }
            is FittingConfirmationIntent.ConfirmDeleteAddress -> deleteAddress()
            else -> Unit
        }
    }

    private fun loadClientAddresses() {
        launch {
            reduce { it.copy(isAddressListLoading = true) }
            val addresses = cartInteractor.loadClientDeliveryAddresses()
            reduce {
                val selectedId = it.selectedClientAddressId
                    ?.takeIf { id -> addresses.any { address -> address.id == id } }
                    ?: addresses.firstOrNull { address -> address.address == it.clientAddress }?.id
                    ?: addresses.firstOrNull()?.id
                it.copy(
                    clientAddresses = addresses,
                    selectedClientAddressId = selectedId,
                    pendingClientAddressId = it.pendingClientAddressId
                        ?.takeIf { id -> addresses.any { address -> address.id == id } }
                        ?: selectedId,
                    isAddressListLoading = false
                )
            }
        }
    }

    private fun searchAddress(query: String) {
        launch {
            reduce {
                when (it.addressSearchQuery) {
                    query -> it.copy(isAddressSuggestionsLoading = true)
                    else -> it
                }
            }
            val suggestions = cartInteractor.searchClientDeliveryAddress(query)
            reduce {
                when (it.addressSearchQuery) {
                    query -> it.copy(
                        addressSuggestions = suggestions,
                        isAddressSuggestionsLoading = false
                    )
                    else -> it
                }
            }
        }
    }

    private fun saveAddress() {
        launch {
            reduce { it.copy(isAddressSaving = true) }
            val form = stateFlow.value.addressForm
            val address = form.clientDeliveryAddress()
            val savedAddress = when {
                form.isEdit -> cartInteractor.updateClientDeliveryAddress(address)
                else -> cartInteractor.createClientDeliveryAddress(address)
            }
            reduce {
                val addresses = (it.clientAddresses.filter { item ->
                    item.id != savedAddress.id
                } + savedAddress).sortedBy { item -> item.id }
                it.copy(
                    clientAddresses = addresses,
                    selectedClientAddressId = savedAddress.id,
                    pendingClientAddressId = savedAddress.id,
                    isAddressFormVisible = false,
                    isAddressSaving = false
                )
            }
            sendSelectionResult(stateFlow.value.pendingClientAddressId, stateFlow.value.clientAddresses)
            MainEventManager.send(BackRoute)
        }
    }

    private fun deleteAddress() {
        launch {
            val addressId = stateFlow.value.deleteAddressId ?: return@launch
            cartInteractor.deleteClientDeliveryAddress(addressId)
            reduce {
                val addresses = it.clientAddresses.filter { address -> address.id != addressId }
                val selectedId = when (it.selectedClientAddressId) {
                    addressId -> addresses.firstOrNull()?.id
                    else -> it.selectedClientAddressId
                }
                it.copy(
                    clientAddresses = addresses,
                    selectedClientAddressId = selectedId,
                    pendingClientAddressId = when (it.pendingClientAddressId) {
                        addressId -> selectedId
                        else -> it.pendingClientAddressId
                    },
                    deleteAddressId = null
                )
            }
        }
    }

    private fun saveSelection() {
        launch {
            val state = stateFlow.value
            val selectedAddressId = state.pendingClientAddressId
            if (selectedAddressId != null) {
                sendSelectionResult(selectedAddressId, state.clientAddresses)
            }
            MainEventManager.send(BackRoute)
        }
    }

    private suspend fun sendSelectionResult(
        selectedClientAddressId: Int?,
        clientAddresses: List<ClientDeliveryAddress>
    ) {
        FittingAddressSelectionResultManager.send(
            FittingAddressSelectionResult(
                confirmationRoute = route.confirmationRoute,
                selectedClientAddressId = selectedClientAddressId,
                clientAddresses = clientAddresses
            )
        )
    }

    override fun catch(throwable: Throwable) {
        reduce {
            it.copy(
                isAddressSaving = false,
                isAddressListLoading = false,
                isAddressSuggestionsLoading = false
            )
        }
        when (throwable) {
            is ClientAddressException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            is ClientException -> launch { send(FittingConfirmationEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FittingAddressSelectionRoute): FittingAddressSelectionViewModel
    }
}
