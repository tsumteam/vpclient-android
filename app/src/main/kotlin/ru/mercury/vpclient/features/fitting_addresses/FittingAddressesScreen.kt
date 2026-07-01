@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_addresses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.fitting_address_actions_sheet.FittingAddressActionsSheet
import ru.mercury.vpclient.features.fitting_address_actions_sheet.intent.FittingAddressActionsSheetIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.FittingAddressDeleteDialog
import ru.mercury.vpclient.features.fitting_address_delete_dialog.intent.FittingAddressDeleteDialogIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.model.FittingAddressDeleteDialogModel
import ru.mercury.vpclient.features.fitting_address_search_sheet.FittingAddressSearchSheet
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesRoute
import ru.mercury.vpclient.features.fitting_address_sheet.FittingAddressSheet
import ru.mercury.vpclient.features.fitting_address_sheet.intent.FittingAddressSheetIntent
import ru.mercury.vpclient.features.fitting_addresses.intent.FittingAddressesIntent
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.domain.mapper.title
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.CirclePlus24
import ru.mercury.vpclient.shared.ui.icons.DotsMenu24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular13

@Composable
fun FittingAddressesScreen(
    route: FittingAddressesRoute,
    viewModel: FittingAddressesViewModel = hiltViewModel<FittingAddressesViewModel, FittingAddressesViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    FittingAddressesScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is FittingConfirmationEvent.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }

    if (state.isAddressActionsSheetVisible) {
        FittingAddressActionsSheet(
            dispatch = { intent ->
                when (intent) {
                    is FittingAddressActionsSheetIntent.EditClick -> {
                        viewModel.dispatch(FittingAddressesIntent.EditAddressClick)
                    }
                    is FittingAddressActionsSheetIntent.DeleteClick -> {
                        state.addressActionAddressId?.let { addressId ->
                            viewModel.dispatch(FittingAddressesIntent.RequestDeleteAddress(addressId))
                        }
                    }
                    is FittingAddressActionsSheetIntent.DismissRequest -> {
                        viewModel.dispatch(FittingAddressesIntent.HideAddressActions)
                    }
                }
            }
        )
    }

    if (state.isAddressFormVisible) {
        FittingAddressSheet(
            state = state.addressForm,
            dispatch = { intent ->
                when (intent) {
                    is FittingAddressSheetIntent.AddressFormValueChange -> {
                        viewModel.dispatch(
                            FittingAddressesIntent.AddressFormValueChange(intent.field, intent.value)
                        )
                    }
                    is FittingAddressSheetIntent.DismissRequest -> {
                        viewModel.dispatch(FittingAddressesIntent.HideAddressForm)
                    }
                    is FittingAddressSheetIntent.OpenAddressSearch -> {
                        viewModel.dispatch(FittingAddressesIntent.OpenAddressSearch)
                    }
                    is FittingAddressSheetIntent.SaveAddressClick -> {
                        viewModel.dispatch(FittingAddressesIntent.SaveAddressClick)
                    }
                }
            },
            snackbarHostStateError = snackbarHostStateError
        )
    }

    if (state.isAddressSearchVisible) {
        FittingAddressSearchSheet(
            initialQuery = state.addressForm.address,
            onDismissRequest = { viewModel.dispatch(FittingAddressesIntent.HideAddressSearch) },
            onSelectAddressSuggestion = { suggestion ->
                viewModel.dispatch(FittingAddressesIntent.SelectAddressSuggestion(suggestion))
            }
        )
    }

    if (state.isAddressDeleteDialogVisible) {
        val address = requireNotNull(state.deleteAddress)
        FittingAddressDeleteDialog(
            state = FittingAddressDeleteDialogModel(
                address = address.title
            ),
            dispatch = { intent ->
                when (intent) {
                    is FittingAddressDeleteDialogIntent.ConfirmClick -> {
                        viewModel.dispatch(FittingAddressesIntent.ConfirmDeleteAddress)
                    }
                    is FittingAddressDeleteDialogIntent.DismissRequest -> {
                        viewModel.dispatch(FittingAddressesIntent.DismissDeleteAddress)
                    }
                }
            }
        )
    }
}

@Composable
private fun FittingAddressesScreenContent(
    state: FittingConfirmationModel,
    dispatch: (FittingAddressesIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FittingAddressesIntent.BackClick) },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            Button(
                onClick = { dispatch(FittingAddressesIntent.SaveAddressSelectionClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.FittingAddressSave),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(bottom = 8.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(
                bottom = 84.dp
            )
        ) {
            item {
                Text(
                    text = stringResource(ClientStrings.FittingAddressSelectDelivery),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 16.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    style = MaterialTheme.typography.regular13.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                )
            }
            state.clientAddresses.forEach { address ->
                item(
                    key = address.id
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { dispatch(FittingAddressesIntent.SelectClientAddress(address.id)) }
                            .padding(start = 16.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.pendingClientAddressId == address.id,
                            onClick = null
                        )

                        Text(
                            text = address.title,
                            modifier = Modifier.weight(1F),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.medium14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 16.sp
                            )
                        )

                        IconButton(
                            onClick = { dispatch(FittingAddressesIntent.OpenAddressActions(address.id)) }
                        ) {
                            Icon(
                                imageVector = DotsMenu24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                item(
                    key = "divider_${address.id}"
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { dispatch(FittingAddressesIntent.AddAddressClick) }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = CirclePlus24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = stringResource(ClientStrings.FittingConfirmationAddAddress),
                        style = MaterialTheme.typography.medium14.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FittingAddressesScreenPreview(
    @PreviewParameter(FittingConfirmationModelPreviewParameterProvider::class) state: FittingConfirmationModel
) {
    FittingAddressesScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class FittingConfirmationModelPreviewParameterProvider: PreviewParameterProvider<FittingConfirmationModel> {
    private val products = sequenceOf(
        CartProduct(
            id = "1",
            detailId = "1",
            itemId = "1",
            colorId = "1",
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            name = "Хлопковая футболка с логотипом",
            article = "MP827743",
            color = "Белый",
            size = "IT 48",
            price = "1 600 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            isForPayment = true,
            priceValue = 1_600_000.0
        ),
        CartProduct(
            id = "2",
            detailId = "2",
            itemId = "2",
            colorId = "2",
            brand = "SAINT LAURENT",
            urlBrandLogo = null,
            name = "Кожаная куртка",
            article = "SL908221",
            color = "Черный",
            size = "FR 38",
            price = "300 000 ₽",
            oldPrice = "400 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            isForPayment = false,
            quantity = 2,
            priceValue = 300_000.0
        ),
        CartProduct(
            id = "3",
            detailId = "3",
            itemId = "3",
            colorId = "3",
            brand = "LORO PIANA",
            urlBrandLogo = null,
            name = "Кашемировый джемпер",
            article = "LP112490",
            color = "Серый",
            size = "M",
            price = "580 000 ₽",
            imageUrl = "",
            isForPayment = false,
            isSold = true,
            isAlternativesPaletteOpen = true,
            alternatives = listOf(
                CartProductAlternative(
                    id = "1",
                    detailId = "1",
                    brand = "LORO PIANA",
                    urlBrandLogo = null,
                    price = "580 000 ₽",
                    imageUrl = "",
                    isOriginal = true
                ),
                CartProductAlternative(
                    id = "2",
                    detailId = "2",
                    brand = "DOLCE&GABBANA",
                    urlBrandLogo = null,
                    price = "1 900 000 ₽",
                    imageUrl = "",
                    isOriginal = false
                )
            ),
            priceValue = 580_000.0
        )
    ).take(3).toList()
    private val firstInterval = FittingConfirmationDeliveryInterval(
        id = "2026-05-13T10:00_2026-05-13T12:00",
        dayId = "2026-05-13",
        dayTitle = "13 мая",
        timeTitle = "10:00-12:00",
        summary = "13 мая с 10:00 до 12:00"
    )
    private val secondInterval = FittingConfirmationDeliveryInterval(
        id = "2026-05-13T12:00_2026-05-13T14:00",
        dayId = "2026-05-13",
        dayTitle = "13 мая",
        timeTitle = "12:00-14:00",
        summary = "13 мая с 12:00 до 14:00"
    )
    private val thirdInterval = FittingConfirmationDeliveryInterval(
        id = "2026-06-13T10:00_2026-06-13T12:00",
        dayId = "2026-06-13",
        dayTitle = "13 июня",
        timeTitle = "10:00-12:00",
        summary = "13 июня с 10:00 до 12:00"
    )
    private val firstDelivery = FittingConfirmationDeliveryGroup(
        id = "delivery_0",
        products = products.take(1),
        intervals = listOf(firstInterval, secondInterval)
    )
    private val secondDelivery = FittingConfirmationDeliveryGroup(
        id = "delivery_1",
        products = products.drop(1),
        intervals = listOf(thirdInterval)
    )
    private val addresses = listOf(
        ClientDeliveryAddressEntity(
            id = 1,
            address = "Москва, Петровка, 2",
            flat = "14"
        ),
        ClientDeliveryAddressEntity(
            id = 2,
            address = "Москва, Рублевское шоссе, 9",
            entrance = "2",
            floor = "5"
        )
    )

    private fun base(products: List<CartProduct>): FittingConfirmationModel {
        return FittingConfirmationModel(
            productIds = products.map { it.id },
            products = products,
            boutiqueAddress = "Барвиха Luxury Village",
            clientAddress = "Москва, Петровка, 2",
            clientAddresses = addresses,
            selectedClientAddressId = addresses.first().id,
            pendingClientAddressId = addresses.first().id,
            selectedPlaceType = FittingConfirmationPlaceType.Boutique,
            deliveryMode = FittingConfirmationDeliveryMode.Multiple,
            singleIntervals = listOf(firstInterval, secondInterval, thirdInterval),
            deliveryGroups = listOf(firstDelivery, secondDelivery),
            selectedSingleDayId = firstInterval.dayId,
            selectedSingleIntervalId = firstInterval.id,
            selectedDeliveryDayIds = mapOf(
                firstDelivery.id to firstInterval.dayId,
                secondDelivery.id to thirdInterval.dayId
            ),
            selectedDeliveryIntervalIds = mapOf(
                firstDelivery.id to firstInterval.id,
                secondDelivery.id to thirdInterval.id
            )
        )
    }

    override val values: Sequence<FittingConfirmationModel> = sequenceOf(
        base(products).copy(
            clientAddresses = emptyList(),
            pendingClientAddressId = null
        ),
        base(products).copy(
            pendingClientAddressId = null
        ),
        base(products),
        base(products).copy(
            selectedClientAddressId = addresses.first().id,
            pendingClientAddressId = addresses.first().id
        ),
        base(products).copy(
            selectedClientAddressId = addresses.first().id,
            pendingClientAddressId = addresses.last().id
        )
    )
}
