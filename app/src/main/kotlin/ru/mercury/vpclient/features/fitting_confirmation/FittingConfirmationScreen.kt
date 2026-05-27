@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_confirmation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.fitting_confirmation.event.FittingConfirmationEvent
import ru.mercury.vpclient.features.fitting_confirmation.intent.FittingConfirmationIntent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationPlaceType
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.LoadingBox
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.FittingConfirmationModelProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium12
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14

private val FittingConfirmationIntervalShape = RoundedCornerShape(4.dp)
private val FittingConfirmationSelectedIntervalShape = RoundedCornerShape(6.dp)

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

    FittingConfirmationScreenContent(
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
}

@Composable
private fun FittingConfirmationScreenContent(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
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
            floatingActionButton = {
                val expandedDeliveryId = state.expandedDeliveryId
                ClientButton(
                    onClick = {
                        when (expandedDeliveryId) {
                            null -> dispatch(FittingConfirmationIntent.ConfirmClick)
                            else -> dispatch(FittingConfirmationIntent.ChangeDeliveryTimeClick(expandedDeliveryId))
                        }
                    },
                    text = stringResource(
                        when (expandedDeliveryId) {
                            null -> ClientStrings.FittingConfirmationConfirm
                            else -> ClientStrings.FittingConfirmationConfirmTime
                        }
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .placeholder(
                            visible = state.isIntervalsLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    enabled = state.isConfirmEnabled
                )
            },
            snackbarHost = {
                SharedSnackbarHost(
                    hostState = snackbarHostStateError,
                    modifier = Modifier.padding(bottom = 8.dp),
                    containerColor = MaterialTheme.colorScheme.error
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 96.dp
                )
            ) {
                if (state.isIntervalsLoading) {
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
                            text = state.boutiqueAddress ?: stringResource(ClientStrings.FittingConfirmationPlaceBoutique),
                            selected = state.selectedPlaceType == FittingConfirmationPlaceType.Boutique,
                            enabled = true,
                            showChevron = false,
                            onClick = {
                                dispatch(
                                    FittingConfirmationIntent.SelectPlace(FittingConfirmationPlaceType.Boutique)
                                )
                            }
                        )
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 48.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    if (state.clientAddress != null) {
                        item {
                            FittingConfirmationPlaceRow(
                                text = state.clientAddress,
                                selected = state.selectedPlaceType == FittingConfirmationPlaceType.Home,
                                enabled = state.isClientAddressAvailable,
                                showChevron = false,
                                onClick = {
                                    dispatch(
                                        FittingConfirmationIntent.SelectPlace(FittingConfirmationPlaceType.Home)
                                    )
                                }
                            )
                        }

                        item {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 48.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }

                    item {
                        FittingConfirmationPlaceRow(
                            text = stringResource(ClientStrings.FittingConfirmationSelectAddress),
                            selected = state.selectedPlaceType == FittingConfirmationPlaceType.Other,
                            enabled = true,
                            showChevron = true,
                            onClick = {
                                dispatch(
                                    FittingConfirmationIntent.SelectPlace(FittingConfirmationPlaceType.Other)
                                )
                            }
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

        LoadingBox(
            isVisible = state.isConfirmLoading
        )
    }
}

@Composable
private fun FittingConfirmationSectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular14.copy(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun FittingConfirmationPlaceRow(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    showChevron: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1F),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium14.copy(
                color = when {
                    enabled -> MaterialTheme.colorScheme.onBackground
                    else -> MaterialTheme.colorScheme.outline
                },
                lineHeight = 16.sp
            )
        )

        if (showChevron) {
            Icon(
                imageVector = ChevronStart24,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { scaleX = -1F },
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun FittingConfirmationScreenLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer(
            modifier = Modifier.padding(top = 16.dp)
        )
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer()
    }
}

@Composable
private fun FittingConfirmationLoadingSpacer(
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(40.dp)
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun FittingConfirmationDeliverySection(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        when {
            state.isIntervalsLoading -> {
                FittingConfirmationIntervalsLoading()
            }
            state.intervalsError != null -> {
                FittingConfirmationInfoText(
                    text = state.intervalsError
                )
            }
            state.isMultipleDeliveryAvailable -> {
                FittingConfirmationMultipleDeliveryContent(
                    state = state,
                    dispatch = dispatch
                )
            }
            else -> {
                FittingConfirmationSingleDeliveryContent(
                    intervals = state.singleIntervals,
                    selectedDayId = state.selectedSingleDayId,
                    selectedIntervalId = state.selectedSingleIntervalId,
                    onDayClick = { dayId -> dispatch(FittingConfirmationIntent.SelectSingleDay(dayId)) },
                    onIntervalClick = { intervalId ->
                        dispatch(FittingConfirmationIntent.SelectSingleInterval(intervalId))
                    }
                )
            }
        }
    }
}

@Composable
private fun FittingConfirmationMultipleDeliveryContent(
    state: FittingConfirmationModel,
    dispatch: (FittingConfirmationIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FittingConfirmationInfoText(
            text = stringResource(ClientStrings.FittingConfirmationMultipleDeliveryInfo),
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp)
        )

        SharedTabRow(
            state = SharedTabRowState(
                selectedIndex = when (state.deliveryMode) {
                    FittingConfirmationDeliveryMode.Multiple -> 0
                    FittingConfirmationDeliveryMode.Single -> 1
                },
                firstTabText = stringResource(ClientStrings.FittingConfirmationMultipleDeliveries),
                secondTabText = stringResource(ClientStrings.FittingConfirmationSingleDelivery),
                onFirstTabClick = {
                    dispatch(
                        FittingConfirmationIntent.SelectDeliveryMode(FittingConfirmationDeliveryMode.Multiple)
                    )
                },
                onSecondTabClick = {
                    dispatch(
                        FittingConfirmationIntent.SelectDeliveryMode(FittingConfirmationDeliveryMode.Single)
                    )
                },
                isLoading = false
            ),
            textStyle = MaterialTheme.typography.medium13.copy(
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(start = 16.dp, top = 18.dp, end = 16.dp)
        )

        when (state.deliveryMode) {
            FittingConfirmationDeliveryMode.Multiple -> {
                state.deliveryGroups.forEach { group ->
                    FittingConfirmationDeliveryGroupCard(
                        group = group,
                        selectedDayId = state.selectedDeliveryDayIds[group.id],
                        selectedIntervalId = state.selectedDeliveryIntervalIds[group.id],
                        expanded = state.expandedDeliveryId == group.id,
                        onChangeTimeClick = {
                            dispatch(FittingConfirmationIntent.ChangeDeliveryTimeClick(group.id))
                        },
                        onDayClick = { dayId ->
                            dispatch(FittingConfirmationIntent.SelectDeliveryDay(group.id, dayId))
                        },
                        onIntervalClick = { intervalId ->
                            dispatch(FittingConfirmationIntent.SelectDeliveryInterval(group.id, intervalId))
                        }
                    )
                }
            }
            FittingConfirmationDeliveryMode.Single -> {
                FittingConfirmationSingleDeliveryContent(
                    intervals = state.singleIntervals,
                    selectedDayId = state.selectedSingleDayId,
                    selectedIntervalId = state.selectedSingleIntervalId,
                    onDayClick = { dayId -> dispatch(FittingConfirmationIntent.SelectSingleDay(dayId)) },
                    onIntervalClick = { intervalId ->
                        dispatch(FittingConfirmationIntent.SelectSingleInterval(intervalId))
                    }
                )
            }
        }
    }
}

@Composable
private fun FittingConfirmationSingleDeliveryContent(
    intervals: List<FittingConfirmationDeliveryInterval>,
    selectedDayId: String?,
    selectedIntervalId: String?,
    onDayClick: (String) -> Unit,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        when {
            intervals.isEmpty() -> {
                FittingConfirmationInfoText(
                    text = stringResource(ClientStrings.FittingConfirmationIntervalsEmpty)
                )
            }
            else -> {
                FittingConfirmationDaysRow(
                    intervals = intervals,
                    selectedDayId = selectedDayId,
                    onDayClick = onDayClick
                )

                FittingConfirmationIntervalsRow(
                    intervals = intervals.filter { interval ->
                        interval.dayId == (selectedDayId ?: intervals.firstOrNull()?.dayId)
                    },
                    selectedIntervalId = selectedIntervalId,
                    onIntervalClick = onIntervalClick
                )
            }
        }
    }
}

@Composable
private fun FittingConfirmationDeliveryGroupCard(
    group: FittingConfirmationDeliveryGroup,
    selectedDayId: String?,
    selectedIntervalId: String?,
    expanded: Boolean,
    onChangeTimeClick: () -> Unit,
    onDayClick: (String) -> Unit,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedInterval = group.intervals.firstOrNull { interval -> interval.id == selectedIntervalId }
    val productsCount = group.products.sumOf { it.quantity }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        append(
                            pluralStringResource(
                                ClientStrings.FittingConfirmationDeliveryProductsCount,
                                productsCount,
                                productsCount
                            )
                        )
                    }
                    append(": ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append(selectedInterval?.summary.orEmpty())
                    }
                },
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.regular14.copy(
                    lineHeight = 20.sp
                )
            )

            AnimatedVisibility(
                visible = !expanded
            ) {
                TextButton(
                    onClick = onChangeTimeClick,
                    modifier = Modifier.height(40.dp),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = stringResource(ClientStrings.FittingConfirmationChangeTime),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.medium13.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Right
                        )
                    )
                }
            }
        }

        if (!expanded) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                FittingConfirmationProductsRow(
                    products = group.products
                )

                FittingConfirmationDaysRow(
                    intervals = group.intervals,
                    selectedDayId = selectedDayId,
                    onDayClick = onDayClick,
                    modifier = Modifier.padding(top = 8.dp)
                )

                FittingConfirmationIntervalsRow(
                    intervals = group.intervals.filter { interval ->
                        interval.dayId == (selectedDayId ?: group.intervals.firstOrNull()?.dayId)
                    },
                    selectedIntervalId = selectedIntervalId,
                    onIntervalClick = onIntervalClick
                )
            }
        }
    }
}

@Composable
private fun FittingConfirmationProductsRow(
    products: List<CartProduct>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            FittingConfirmationProductCard(
                product = product
            )
        }
    }
}

@Composable
private fun FittingConfirmationProductCard(
    product: CartProduct,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.size(width = 112.dp, height = 149.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClientAsyncImage(
            imageUrl = product.imageUrl,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 14.dp)
                .size(width = 62.dp, height = 96.dp)
        )

        BrandBox(
            entity = BrandEntity(
                brand = product.brand,
                urlBrandLogo = product.urlBrandLogo
            ),
            modifier = Modifier
                .padding(top = 4.dp)
                .size(width = 96.dp, height = 20.dp),
            style = MaterialTheme.typography.regular11
        )

        Text(
            text = product.price,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(96.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular11.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun FittingConfirmationDaysRow(
    intervals: List<FittingConfirmationDeliveryInterval>,
    selectedDayId: String?,
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    val days = intervals.distinctBy { interval -> interval.dayId }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(days) { index, interval ->
            val dayId = selectedDayId ?: days.firstOrNull()?.dayId

            FittingConfirmationChip(
                text = interval.dayTitle,
                selected = dayId == interval.dayId || (dayId == null && index == 0),
                height = 52.dp,
                onClick = { onDayClick(interval.dayId) }
            )
        }
    }
}

@Composable
private fun FittingConfirmationIntervalsRow(
    intervals: List<FittingConfirmationDeliveryInterval>,
    selectedIntervalId: String?,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = intervals,
            key = { interval -> interval.id }
        ) { interval ->
            FittingConfirmationChip(
                text = interval.timeTitle,
                selected = selectedIntervalId == interval.id,
                height = 27.dp,
                onClick = { onIntervalClick(interval.id) }
            )
        }
    }
}

@Composable
private fun FittingConfirmationChip(
    text: String,
    selected: Boolean,
    height: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipContent: @Composable () -> Unit = {
        Surface(
            modifier = Modifier.height(height),
            shape = FittingConfirmationIntervalShape,
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Box(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    style = MaterialTheme.typography.medium12.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .height(height + 6.dp)
            .border(
                width = 2.dp,
                color = when {
                    selected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.background
                },
                shape = FittingConfirmationSelectedIntervalShape
            )
            .clickable(onClick = onClick)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        chipContent()
    }
}

@Composable
private fun FittingConfirmationIntervalsLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(72.dp, 92.dp, 84.dp).forEach { width ->
                Box(
                    modifier = Modifier
                        .size(width = width, height = 58.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = FittingConfirmationSelectedIntervalShape
                        )
                )
            }
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(76.dp, 88.dp, 96.dp).forEach { width ->
                Box(
                    modifier = Modifier
                        .size(width = width, height = 33.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = FittingConfirmationSelectedIntervalShape
                        )
                )
            }
        }
    }
}

@Composable
private fun FittingConfirmationInfoText(
    text: String,
    modifier: Modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.regular14.copy(
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp,
            textAlign = TextAlign.Start
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingConfirmationScreenLoadingPreview() {
    FittingConfirmationScreenLoading()
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingConfirmationProductCardPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    FittingConfirmationProductCard(
        product = product
    )
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingConfirmationIntervalsLoadingPreview() {
    FittingConfirmationIntervalsLoading()
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingConfirmationScreenContentPreview(
    @PreviewParameter(FittingConfirmationModelProvider::class) state: FittingConfirmationModel
) {
    FittingConfirmationScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}
