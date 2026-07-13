@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
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
import ru.mercury.vpclient.features.details.event.DetailsEvent
import ru.mercury.vpclient.features.details.intent.DetailsIntent
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.details_cart_added_sheet.DetailsCartAddedSheet
import ru.mercury.vpclient.features.details_cart_added_sheet.intent.DetailsCartAddedSheetIntent
import ru.mercury.vpclient.features.details_cart_added_sheet.model.DetailsCartAddedSheetModel
import ru.mercury.vpclient.features.details_message_sheet.DetailsChatSheet
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsChatIntent
import ru.mercury.vpclient.features.details_message_sheet.model.DetailsChatModel
import ru.mercury.vpclient.features.details_wear_with_sheet.DetailsWearWithSheet
import ru.mercury.vpclient.features.details_wear_with_sheet.intent.DetailsWearWithSheetIntent
import ru.mercury.vpclient.features.details_wear_with_sheet.model.DetailsWearWithSheetModel
import ru.mercury.vpclient.features.size_sheet.SizeSheet
import ru.mercury.vpclient.features.size_sheet.intent.SizeSheetIntent
import ru.mercury.vpclient.features.size_sheet.model.SizeSheetModel
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.DetailsMediaItem
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductButtonEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductOtherColorEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.brands.BrandBox
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.details.DetailsColorImageSelector
import ru.mercury.vpclient.shared.ui.components.details.DetailsCompleteSetSection
import ru.mercury.vpclient.shared.ui.components.details.DetailsFieldRow
import ru.mercury.vpclient.shared.ui.components.details.DetailsFieldRowState
import ru.mercury.vpclient.shared.ui.components.details.DetailsOutfitButton
import ru.mercury.vpclient.shared.ui.components.details.DetailsPagerIndicator
import ru.mercury.vpclient.shared.ui.components.details.DetailsProductInfoBox
import ru.mercury.vpclient.shared.ui.components.details.DetailsProductInfoBoxState
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeSelector
import ru.mercury.vpclient.shared.ui.components.details.DetailsWearWithSection
import ru.mercury.vpclient.shared.ui.components.details.DetailsWearWithSectionState
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.components.video.VideoPlayer
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.livretRegular15
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun DetailsScreen(
    route: DetailsRoute,
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarHostStateError = remember { SnackbarHostState() }

    DetailsScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostState = snackbarHostState,
        snackbarHostStateError = snackbarHostStateError
    )

    if (state.isMessageSheetVisible) {
        DetailsChatSheet(
            state = DetailsChatModel(
                productEntity = state.productEntity
            ),
            dispatch = { intent ->
                when (intent) {
                    is DetailsChatIntent.CommentChange -> Unit
                    is DetailsChatIntent.SendClick -> {
                        viewModel.dispatch(DetailsIntent.HideMessageSheet)
                    }
                    is DetailsChatIntent.DismissRequest -> {
                        viewModel.dispatch(DetailsIntent.HideMessageSheet)
                    }
                }
            }
        )
    }

    if (state.isCartAddedSheetVisible) {
        DetailsCartAddedSheet(
            state = DetailsCartAddedSheetModel(
                productEntity = state.productEntity
            ),
            dispatch = { intent ->
                when (intent) {
                    is DetailsCartAddedSheetIntent.ContinueShoppingClick -> {
                        viewModel.dispatch(DetailsIntent.HideCartAddedSheet)
                    }
                    is DetailsCartAddedSheetIntent.CartClick -> {
                        viewModel.dispatch(DetailsIntent.CartAddedSheetCartClick)
                    }
                    is DetailsCartAddedSheetIntent.DismissRequest -> {
                        viewModel.dispatch(DetailsIntent.HideCartAddedSheet)
                    }
                }
            }
        )
    }

    if (state.isWearWithSheetVisible) {
        DetailsWearWithSheet(
            state = DetailsWearWithSheetModel(
                products = state.wearWithProducts,
                basketProductIds = state.basketProductIds,
                basketProductKeys = state.basketProductKeys
            ),
            dispatch = { intent ->
                when (intent) {
                    is DetailsWearWithSheetIntent.ProductClick -> {
                        viewModel.dispatch(DetailsIntent.ProductClick(intent.id))
                    }
                    is DetailsWearWithSheetIntent.ProductBasketClick -> {
                        viewModel.dispatch(DetailsIntent.ProductBasketClick(intent.product))
                    }
                    is DetailsWearWithSheetIntent.DismissRequest -> {
                        viewModel.dispatch(DetailsIntent.HideWearWithSheet)
                    }
                }
            }
        )
    }

    if (state.isSizePickerSheetVisible) {
        SizeSheet(
            state = SizeSheetModel(
                sizeSelectorState = state.sizePickerState
            ),
            dispatch = { intent ->
                when (intent) {
                    is SizeSheetIntent.SizeClick -> {
                        viewModel.dispatch(DetailsIntent.SizeClick(intent.index))
                    }
                    is SizeSheetIntent.SizeTableClick -> {
                        viewModel.dispatch(DetailsIntent.SizeTableClick)
                    }
                    is SizeSheetIntent.AddToBasketClick -> {
                        viewModel.dispatch(DetailsIntent.AddToBasketClick)
                    }
                    is SizeSheetIntent.DismissRequest -> {
                        viewModel.dispatch(DetailsIntent.HideSizePicker)
                    }
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is DetailsEvent.SnackbarMessage -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostState.showSnackbar(resources.getString(event.messageRes)) }
            }
            is DetailsEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun DetailsScreenContent(
    state: DetailsModel,
    dispatch: (DetailsIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarHostStateError: SnackbarHostState
) {
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val toolbarBrandOffsetPx = with(LocalDensity.current) { 52.dp.roundToPx() }
    val isToolbarBrandVisible by remember(lazyListState, toolbarBrandOffsetPx) {
        derivedStateOf {
            when {
                lazyListState.firstVisibleItemIndex > 4 -> true
                lazyListState.firstVisibleItemIndex < 4 -> false
                else -> lazyListState.firstVisibleItemScrollOffset >= toolbarBrandOffsetPx
            }
        }
    }

    val pagerItems = remember(
        state.productEntity.colorImageUrls,
        state.productEntity.otherColors,
        state.productEntity.urlItemVideo,
        state.selectedOtherColorIndex
    ) {
        val images = state.pagerImageUrls.map { DetailsMediaItem.Image(it) }
        val video = state.selectedColorVideoUrl?.let { listOf(DetailsMediaItem.Video(it)) }.orEmpty()
        images + video
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { if (pagerItems.isEmpty()) 0 else Int.MAX_VALUE }
    )

    LaunchedEffect(pagerItems.size) {
        if (pagerItems.isNotEmpty()) {
            val mid = Int.MAX_VALUE / 2
            pagerState.scrollToPage(mid - mid % pagerItems.size)
        }
    }

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SharedAnimatedVisibility(
                        visible = !state.isLoading && isToolbarBrandVisible
                    ) {
                        BrandBox(
                            entity = BrandEntity(
                                brand = state.productEntity.brand.orEmpty(),
                                urlBrandLogo = state.productEntity.urlBrandLogo
                            )
                        )
                    }
                },
                modifier = when {
                    state.isLoading -> Modifier
                    else -> Modifier.clickableWithoutRipple(
                        onClick = { scope.launch { lazyListState.animateScrollToItem(0) } }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(DetailsIntent.BackClick) },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    if (state.isFittingButtonVisible) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.isFittingBadgeVisible,
                            onClick = { dispatch(DetailsIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.isCartBadgeVisible,
                        onClick = { dispatch(DetailsIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.isMessengerBadgeVisible,
                        onClick = { dispatch(DetailsIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
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
                    onClick = { dispatch(DetailsIntent.AddToBasketClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .placeholder(
                            visible = state.isLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(state.addToBasketButtonText),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }
        },
        snackbarHost = {
            Box {
                SharedSnackbarHost(
                    hostState = snackbarHostState
                )

                SharedSnackbarHost(
                    hostState = snackbarHostStateError,
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    userScrollEnabled = false
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 31.dp, top = 16.dp, end = 31.dp)
                                .aspectRatio(3F / 4F)
                                .clip(RoundedCornerShape(4.dp))
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(horizontal = 64.dp)
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 64.dp, top = 54.dp, end = 64.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(.8F)
                                        .height(14.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(top = 6.dp)
                                        .fillMaxWidth(.6F)
                                        .height(14.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                SharedLazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(bottom = 40.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3F / 4F)
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                userScrollEnabled = pagerItems.size > 1
                            ) { page ->
                                when (val item = pagerItems[page % pagerItems.size]) {
                                    is DetailsMediaItem.Image -> {
                                        ClientAsyncImage(
                                            imageUrl = item.url,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clickable { dispatch(DetailsIntent.OpenMedia(page % pagerItems.size)) },
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                    is DetailsMediaItem.Video -> {
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp)
                                                .fillMaxSize()
                                        ) {
                                            VideoPlayer(
                                                videoUrl = item.url,
                                                isVisible = pagerState.currentPage == page,
                                                modifier = Modifier.fillMaxSize()
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .matchParentSize()
                                                    .clickable { dispatch(DetailsIntent.OpenMedia(page % pagerItems.size)) }
                                            )
                                        }
                                    }
                                }
                            }

                            if (state.isWearWithButtonVisible) {
                                DetailsOutfitButton(
                                    onClick = { dispatch(DetailsIntent.ShowWearWithSheet) },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(end = 16.dp, bottom = 39.dp)
                                )
                            }
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                    }
                    item {
                        DetailsPagerIndicator(
                            pagerState = pagerState,
                            pageCount = pagerItems.size,
                            showVideoIcon = state.hasVideo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            pageIndexMapping = { it % pagerItems.size },
                            onVideoClick = { dispatch(DetailsIntent.OpenVideo) }
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                    }
                    item {
                        DetailsProductInfoBox(
                            state = DetailsProductInfoBoxState(
                                productEntity = state.productEntity,
                                availabilityText = state.noSizeAvailabilityText,
                                onMessageClick = { dispatch(DetailsIntent.MessageClick) }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (state.isSizePickerVisible) {
                        item {
                            DetailsSizeSelector(
                                state = state.sizePickerState.copy(
                                    onSizeClick = { dispatch(DetailsIntent.SizeClick(it)) },
                                    onSizeTableClick = { dispatch(DetailsIntent.SizeTableClick) }
                                )
                            )
                        }
                    }
                    if (state.isProductColorImagesBoxVisible) {
                        item {
                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )
                        }
                        item {
                            DetailsColorImageSelector(
                                colorImageUrls = state.selectorColorImageUrls,
                                onColorClick = {
                                    scope.launch {
                                        lazyListState.animateScrollToItem(0)
                                    }
                                    dispatch(DetailsIntent.ColorClick(it))
                                }
                            )
                        }
                    }
                    if (state.isDescriptionTextVisible) {
                        item {
                            Text(
                                text = state.descriptionText.orEmpty(),
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    letterSpacing = .2.sp
                                )
                            )
                        }
                    }
                    state.detailFields.forEachIndexed { index, field ->
                        item {
                            DetailsFieldRow(
                                state = DetailsFieldRowState(
                                    field = field,
                                    onCopyClick = { dispatch(DetailsIntent.FieldCopyClick(it)) }
                                ),
                                modifier = Modifier.padding(start = 16.dp, top = 24.dp)
                            )
                        }
                        if (index != state.detailFields.lastIndex) {
                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                    if (state.isWearWithBoxVisible) {
                        item {
                            DetailsWearWithSection(
                                state = DetailsWearWithSectionState(
                                    products = state.wearWithProducts,
                                    onProductClick = { id -> dispatch(DetailsIntent.ProductClick(id)) },
                                    isProductInBasket = state::isProductInBasket,
                                    onProductBasketClick = { dispatch(DetailsIntent.ProductBasketClick(it)) }
                                )
                            )
                        }
                    }
                    if (state.isCompleteSetBoxVisible) {
                        item {
                            DetailsCompleteSetSection(
                                products = state.completeSetProducts,
                                onProductClick = { id -> dispatch(DetailsIntent.ProductClick(id)) },
                                isProductInBasket = state::isProductInBasket,
                                onProductBasketClick = { dispatch(DetailsIntent.ProductBasketClick(it)) }
                            )
                        }
                    }
                    state.productEntity.buttons.forEachIndexed { index, button ->
                        item {
                            OutlinedButton(
                                onClick = { dispatch(DetailsIntent.ButtonClick(index)) },
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                    disabledContainerColor = MaterialTheme.colorScheme.background,
                                    disabledContentColor = MaterialTheme.colorScheme.onBackground
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = button.title,
                                    style = MaterialTheme.typography.livretRegular15.copy(
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Preview(heightDp = 2000, showBackground = true)
@Composable
private fun DetailsScreenContentPreview(
    @PreviewParameter(DetailsModelPreviewParameterProvider::class) state: DetailsModel
) {
    DetailsScreenContent(
        state = state,
        dispatch = {},
        snackbarHostState = remember { SnackbarHostState() },
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class DetailsModelPreviewParameterProvider: PreviewParameterProvider<DetailsModel> {
    override val values: Sequence<DetailsModel> = sequenceOf(
        DetailsModel(),
        DetailsModel(
            productEntity = ProductEntity.Empty.copy(
                id = "preview",
                name = "Куртка из кожи",
                price = 129_900.0,
                itemId = "5558447",
                brand = "SAINT LAURENT",
                article = "BRG-CARVE-STEER",
                longDescription = "Куртка прямого кроя с лаконичной отделкой и мягкой фактурой.",
                productionStructure = "натуральная кожа 100%",
                country = "Италия",
                technicalDescription = "Длина изделия 62 см, длина рукава 64 см.",
                breadcrumbs = listOf("Каталог", "Женское", "Одежда", "Куртки"),
                buttons = listOf(
                    ProductButtonEntity(title = "Женская одежда"),
                    ProductButtonEntity(title = "Куртки")
                ),
                colorImageUrls = listOf("", "", "", "", "", "", "", "", ""),
                availableSizes = ProductAvailableSizesEntity(
                    items = listOf(
                        ProductAvailableSizeEntity(
                            sizeId = "36",
                            russianSize = "34",
                            sizeFullName = "IT 36",
                            inStock = true
                        ),
                        ProductAvailableSizeEntity(
                            sizeId = "38",
                            russianSize = "36",
                            sizeFullName = "IT 38",
                            inStock = true
                        ),
                        ProductAvailableSizeEntity(
                            sizeId = "40",
                            russianSize = "38",
                            sizeFullName = "IT 40",
                            inStock = false
                        )
                    ),
                    countryCode = "IT",
                    sizeTableTitle = "Таблица размеров",
                    sizeTableUrl = "https://example.com/size-table"
                ),
                otherColors = listOf(
                    ProductOtherColorEntity(
                        imageUrls = listOf("")
                    ),
                    ProductOtherColorEntity(
                        imageUrls = listOf("")
                    )
                ),
                hasWearWith = true,
                wearWithButtonEnabled = true
            ),
            selectedSizeId = "40"
        )
    )
}
