@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import ru.mercury.vpclient.features.fitting_address_search_sheet.intent.FittingAddressSearchSheetIntent
import ru.mercury.vpclient.features.fitting_address_search_sheet.model.FittingAddressSearchModel
import ru.mercury.vpclient.features.fitting_address_sheet.FittingAddressSheet
import ru.mercury.vpclient.features.fitting_address_sheet.intent.FittingAddressSheetIntent
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.LoadingBox
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationDeliverySection
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationPlaceRow
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationPlaceRowState
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationScreenLoading
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationSectionTitle
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled

@Composable
fun FittingConfirmationScreen(
    route: FittingConfirmationRoute,
    viewModel: FittingConfirmationViewModel = hiltViewModel<FittingConfirmationViewModel, FittingConfirmationViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FittingConfirmationMainContent(
            state = state,
            dispatch = viewModel::dispatch,
            snackbarHostStateError = snackbarHostStateError
        )

        LoadingBox(
            isVisible = state.isConfirmLoading ||
                state.isAddressSaving ||
                (state.isIntervalsLoading && !state.isInitialIntervalsLoading)
        )
    }

    if (state.addressActionAddress != null) {
        FittingAddressActionsSheet(
            dispatch = { intent ->
                when (intent) {
                    is FittingAddressActionsSheetIntent.EditClick -> {
                        viewModel.dispatch(FittingConfirmationIntent.EditAddressClick)
                    }
                    is FittingAddressActionsSheetIntent.DeleteClick -> {
                        state.addressActionAddressId?.let { addressId ->
                            viewModel.dispatch(FittingConfirmationIntent.RequestDeleteAddress(addressId))
                        }
                    }
                    is FittingAddressActionsSheetIntent.DismissRequest -> {
                        viewModel.dispatch(FittingConfirmationIntent.HideAddressActions)
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
                            FittingConfirmationIntent.AddressFormValueChange(intent.field, intent.value)
                        )
                    }
                    is FittingAddressSheetIntent.DismissRequest -> {
                        viewModel.dispatch(FittingConfirmationIntent.HideAddressForm)
                    }
                    is FittingAddressSheetIntent.OpenAddressSearch -> {
                        viewModel.dispatch(FittingConfirmationIntent.OpenAddressSearch)
                    }
                    is FittingAddressSheetIntent.SaveAddressClick -> {
                        viewModel.dispatch(FittingConfirmationIntent.SaveAddressClick)
                    }
                }
            },
            snackbarHostStateError = snackbarHostStateError
        )
    }

    if (state.isAddressSearchVisible) {
        FittingAddressSearchSheet(
            state = FittingAddressSearchModel(
                query = state.addressSearchQuery,
                suggestions = state.addressSuggestions,
                isSuggestionsLoading = state.isAddressSuggestionsLoading
            ),
            dispatch = { intent ->
                when (intent) {
                    is FittingAddressSearchSheetIntent.DismissRequest -> {
                        viewModel.dispatch(FittingConfirmationIntent.HideAddressSearch)
                    }
                    is FittingAddressSearchSheetIntent.QueryChange -> {
                        viewModel.dispatch(FittingConfirmationIntent.AddressSearchQueryChange(intent.value))
                    }
                    is FittingAddressSearchSheetIntent.SelectAddressSuggestion -> {
                        viewModel.dispatch(FittingConfirmationIntent.SelectAddressSuggestion(intent.suggestion))
                    }
                }
            }
        )
    }

    state.deleteAddress?.let { address ->
        FittingAddressDeleteDialog(
            state = FittingAddressDeleteDialogModel(
                address = address.title
            ),
            dispatch = { intent ->
                when (intent) {
                    is FittingAddressDeleteDialogIntent.ConfirmClick -> {
                        viewModel.dispatch(FittingConfirmationIntent.ConfirmDeleteAddress)
                    }
                    is FittingAddressDeleteDialogIntent.DismissRequest -> {
                        viewModel.dispatch(FittingConfirmationIntent.DismissDeleteAddress)
                    }
                }
            }
        )
    }

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
}

@Composable
private fun FittingConfirmationMainContent(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FittingConfirmationIntent.BackClick) },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Button(
                    onClick = {
                        when (state.expandedDeliveryId) {
                            null -> dispatch(FittingConfirmationIntent.ConfirmClick)
                            else -> dispatch(FittingConfirmationIntent.ChangeDeliveryTimeClick(state.expandedDeliveryId))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .placeholder(
                            visible = state.isInitialIntervalsLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    enabled = state.isConfirmEnabled,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.disabled,
                        disabledContentColor = MaterialTheme.colorScheme.onDisabled
                    )
                ) {
                    Text(
                        text = stringResource(
                            when (state.expandedDeliveryId) {
                                null -> ClientStrings.FittingConfirmationConfirm
                                else -> ClientStrings.FittingConfirmationConfirmTime
                            }
                        ),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
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
            contentPadding = innerPadding + PaddingValues(bottom = 8.dp)
        ) {
            if (state.isInitialIntervalsLoading) {
                item {
                    FittingConfirmationScreenLoading()
                }
            } else {
                item {
                    FittingConfirmationSectionTitle(
                        text = stringResource(ClientStrings.FittingConfirmationPlaceTitle)
                    )
                }
                item {
                    FittingConfirmationPlaceRow(
                        state = FittingConfirmationPlaceRowState(
                            text = state.boutiqueAddress
                                ?: stringResource(ClientStrings.FittingConfirmationPlaceBoutique),
                            selected = state.selectedPlaceType == FittingConfirmationPlaceType.Boutique,
                            enabled = true,
                            showChevron = false
                        ),
                        onClick = {
                            dispatch(FittingConfirmationIntent.SelectPlace(FittingConfirmationPlaceType.Boutique))
                        },
                        onChevronClick = { dispatch(FittingConfirmationIntent.OpenAddressSelection) }
                    )
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                item {
                    FittingConfirmationPlaceRow(
                        state = FittingConfirmationPlaceRowState(
                            text = state.displayedClientAddress
                                ?: stringResource(ClientStrings.FittingConfirmationSelectAddress),
                            selected = state.selectedPlaceType == FittingConfirmationPlaceType.Home,
                            enabled = state.isClientAddressAvailable,
                            showChevron = true
                        ),
                        onClick = {
                            dispatch(FittingConfirmationIntent.SelectPlace(FittingConfirmationPlaceType.Home))
                        },
                        onChevronClick = { dispatch(FittingConfirmationIntent.OpenAddressSelection) }
                    )
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                item {
                    FittingConfirmationDeliverySection(
                        state = state,
                        dispatch = dispatch
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FittingConfirmationMainContentPreview(
    @PreviewParameter(FittingConfirmationModelPreviewParameterProvider::class) state: FittingConfirmationModel
) {
    FittingConfirmationMainContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = SnackbarHostState()
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

    override val values: Sequence<FittingConfirmationModel> = sequenceOf(
        base(products).copy(
            clientAddress = null,
            selectedPlaceType = FittingConfirmationPlaceType.Home
        ),
        base(products).copy(
            deliveryMode = FittingConfirmationDeliveryMode.Single,
            deliveryGroups = emptyList()
        ),
        base(products),
        base(products).copy(
            expandedDeliveryId = firstDelivery.id
        ),
        base(products).copy(
            isIntervalsLoading = true,
            isInitialIntervalsLoading = true,
            singleIntervals = emptyList(),
            deliveryGroups = emptyList()
        )
    )

    private fun base(products: List<CartProduct>): FittingConfirmationModel {
        return FittingConfirmationModel(
            route = FittingConfirmationRoute(productIds = products.map { it.id }),
            products = products,
            boutiqueAddress = "Барвиха Luxury Village",
            clientAddress = "Москва, Петровка, 2",
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
            ),
            isInitialIntervalsLoading = false
        )
    }
}
