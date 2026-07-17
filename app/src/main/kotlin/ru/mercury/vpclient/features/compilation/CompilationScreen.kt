@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import ru.mercury.vpclient.features.compilation.event.CompilationEvent
import ru.mercury.vpclient.features.compilation.intent.CompilationIntent
import ru.mercury.vpclient.features.compilation.model.CompilationModel
import ru.mercury.vpclient.features.compilation.navigation.CompilationRoute
import ru.mercury.vpclient.features.compilation_actions_sheet.CompilationActionsSheet
import ru.mercury.vpclient.features.compilation_actions_sheet.intent.CompilationActionsSheetIntent
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.CompilationAddToBasketSheet
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.intent.CompilationAddToBasketSheetIntent
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.model.CompilationAddToBasketSheetModel
import ru.mercury.vpclient.features.compilation_benefit_sheet.CompilationBenefitSheet
import ru.mercury.vpclient.features.compilation_benefit_sheet.intent.CompilationBenefitSheetIntent
import ru.mercury.vpclient.features.compilation_benefit_sheet.model.CompilationBenefitSheetModel
import ru.mercury.vpclient.features.compilation_cart_added_sheet.CompilationCartAddedSheet
import ru.mercury.vpclient.features.compilation_cart_added_sheet.intent.CompilationCartAddedSheetIntent
import ru.mercury.vpclient.features.compilation_cart_added_sheet.model.CompilationCartAddedSheetModel
import ru.mercury.vpclient.features.compilation_chat_sheet.CompilationChatSheet
import ru.mercury.vpclient.features.compilation_chat_sheet.intent.CompilationChatIntent
import ru.mercury.vpclient.features.compilation_chat_sheet.model.CompilationChatModel
import ru.mercury.vpclient.features.details_message_sheet.DetailsChatSheet
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsChatIntent
import ru.mercury.vpclient.features.details_message_sheet.model.DetailsChatModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.domain.mapper.messageSheetProductEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationPromotionBanner
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationPromotionBannerState
import ru.mercury.vpclient.shared.ui.components.details.DetailsPagerIndicator
import ru.mercury.vpclient.shared.ui.components.product.ProductCard
import ru.mercury.vpclient.shared.ui.components.product.ProductCardState
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.DotsMenu24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.livretRegular13
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular13

@Composable
fun CompilationScreen(
    route: CompilationRoute,
    viewModel: CompilationViewModel = hiltViewModel<CompilationViewModel, CompilationViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    CompilationScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostState = snackbarHostState
    )

    if (state.isCompilationActionsSheetVisible) {
        CompilationActionsSheet(
            dispatch = { intent ->
                when (intent) {
                    is CompilationActionsSheetIntent.ShowCompilationChatSheet -> {
                        viewModel.dispatch(CompilationIntent.ShowCompilationChatSheet)
                    }
                    is CompilationActionsSheetIntent.ShowAddToBasketDialog -> {
                        viewModel.dispatch(CompilationIntent.ShowAddToBasketDialog)
                    }
                    is CompilationActionsSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CompilationIntent.HideMenuDialog)
                    }
                }
            }
        )
    }

    if (state.isCompilationChatSheetVisible) {
        CompilationChatSheet(
            state = CompilationChatModel(
                compilationEntity = state.compilationChatEntity,
                selectedLookTitle = state.selectedLookTitle
            ),
            dispatch = { intent ->
                when (intent) {
                    is CompilationChatIntent.DismissRequest -> {
                        viewModel.dispatch(CompilationIntent.HideCompilationChatSheet)
                    }
                    is CompilationChatIntent.CommentChange -> Unit
                    is CompilationChatIntent.SendClick -> {
                        viewModel.dispatch(CompilationIntent.CompilationChatSendClick(intent.comment))
                    }
                }
            }
        )
    }

    if (state.isMessageSheetVisible) {
        val messageSheetProductEntity = state.messageSheetProductEntity?.messageSheetProductEntity()
        if (messageSheetProductEntity != null) {
            DetailsChatSheet(
                state = DetailsChatModel(
                    productEntity = messageSheetProductEntity
                ),
                dispatch = { intent ->
                    when (intent) {
                        is DetailsChatIntent.CommentChange -> Unit
                        is DetailsChatIntent.SendClick,
                        is DetailsChatIntent.DismissRequest -> {
                            viewModel.dispatch(CompilationIntent.HideMessageSheet)
                        }
                    }
                }
            )
        }
    }

    if (state.isCompilationAddToBasketSheetVisible) {
        CompilationAddToBasketSheet(
            state = CompilationAddToBasketSheetModel(
                productEntities = state.selectedLookAddToBasketProductEntities,
                selectedProductIds = state.addToBasketDialogSelectedProductIds,
                availableSizes = state.addToBasketDialogAvailableSizes,
                oneSizeProductIds = state.addToBasketDialogOneSizeProductIds,
                isLoading = state.isAddToBasketLoading
            ),
            dispatch = { intent ->
                when (intent) {
                    is CompilationAddToBasketSheetIntent.AddToBasketClick -> {
                        viewModel.dispatch(CompilationIntent.AddToBasketClick)
                    }
                    is CompilationAddToBasketSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CompilationIntent.HideAddToBasketDialog)
                    }
                    is CompilationAddToBasketSheetIntent.AddToBasketProductCheckedChange -> {
                        viewModel.dispatch(
                            CompilationIntent.AddToBasketProductCheckedChange(
                                productId = intent.productId,
                                checked = intent.checked
                            )
                        )
                    }
                }
            }
        )
    }

    if (state.isCompilationCartAddedSheetVisible) {
        CompilationCartAddedSheet(
            state = CompilationCartAddedSheetModel(
                pageEntity = state.selectedPageEntity ?: CompilationPreviewPageEntity.Empty
            ),
            dispatch = { intent ->
                when (intent) {
                    is CompilationCartAddedSheetIntent.ReturnToCompilationClick,
                    is CompilationCartAddedSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CompilationIntent.HideCartAddedSheet)
                    }
                    is CompilationCartAddedSheetIntent.CartClick -> {
                        viewModel.dispatch(CompilationIntent.CartAddedSheetCartClick)
                    }
                }
            }
        )
    }

    if (state.isCompilationBenefitSheetVisible) {
        CompilationBenefitSheet(
            state = CompilationBenefitSheetModel(
                productEntities = state.selectedLookProductEntities
            ),
            dispatch = { intent ->
                when (intent) {
                    is CompilationBenefitSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CompilationIntent.HideBenefitSheet)
                    }
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is CompilationEvent.SnackbarErrorMessage -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun CompilationScreenContent(
    state: CompilationModel,
    dispatch: (CompilationIntent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            when {
                state.compilationPreviewPageEntities.isEmpty() -> 0
                state.compilationPreviewPageEntities.size == 1 -> 1
                else -> Int.MAX_VALUE
            }
        }
    )

    LaunchedEffect(state.compilationPreviewPageEntities.size) {
        if (state.compilationPreviewPageEntities.size > 1) {
            val mid = Int.MAX_VALUE / 2
            pagerState.scrollToPage(mid - mid % state.compilationPreviewPageEntities.size)
        }
    }

    LaunchedEffect(pagerState.currentPage, state.compilationPreviewPageEntities.size) {
        if (state.compilationPreviewPageEntities.isNotEmpty()) {
            dispatch(CompilationIntent.PageChange(pagerState.currentPage % state.compilationPreviewPageEntities.size))
        }
    }

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.compilationName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.livretMedium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(CompilationIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { dispatch(CompilationIntent.MenuClick) },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = DotsMenu24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
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
                    onClick = { dispatch(CompilationIntent.ShowAddToBasketDialog) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !state.isAddToBasketLoading,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.DetailsAddToBasket),
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
                hostState = snackbarHostState,
                containerColor = MaterialTheme.colorScheme.error
            )
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
                                .height(40.dp)
                                .padding(horizontal = 80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3F / 4F)
                                .placeholder()
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
                                Spacer(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .placeholder(shape = CircleShape)
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.selectedLookTitle,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.livretRegular13.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3F / 4F)
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                userScrollEnabled = state.compilationPreviewPageEntities.size > 1
                            ) { page ->
                                val pageIndex = when {
                                    state.compilationPreviewPageEntities.size > 1 -> {
                                        page % state.compilationPreviewPageEntities.size
                                    }
                                    else -> page
                                }
                                val item = state.compilationPreviewPageEntities[pageIndex]

                                ClientAsyncImage(
                                    imageUrl = item.imageUrl,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { dispatch(CompilationIntent.ImageClick(pageIndex)) },
                                    contentScale = ContentScale.Fit
                                )
                            }

                            if (state.selectedLookNumberText.isNotEmpty()) {
                                Text(
                                    text = state.selectedLookNumberText,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 16.dp, end = 16.dp),
                                    style = MaterialTheme.typography.regular13.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                    if (state.compilationPreviewPageEntities.size > 1) {
                        item {
                            DetailsPagerIndicator(
                                pagerState = pagerState,
                                pageCount = state.compilationPreviewPageEntities.size,
                                showVideoIcon = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                pageIndexMapping = { it % state.compilationPreviewPageEntities.size }
                            )
                        }
                    }
                    if (state.isPromotionVisible) {
                        item {
                            CompilationPromotionBanner(
                                state = CompilationPromotionBannerState(
                                    text = stringResource(
                                        ClientStrings.CompilationPreviewPromotionText,
                                        state.selectedLookPromotionNameText,
                                        state.selectedLookBenefitAmountText
                                    ),
                                    detailsText = stringResource(ClientStrings.CompilationPreviewPromotionDetails),
                                    onClick = { dispatch(CompilationIntent.ShowBenefitSheet) }
                                ),
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                            )
                        }
                    }
                    items(
                        items = state.selectedLookProductEntities.chunked(2)
                    ) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            row.forEach { product ->
                                ProductCard(
                                    state = ProductCardState(
                                        entity = product,
                                        isInBasket = state.isProductInBasket(product),
                                        onClick = { dispatch(CompilationIntent.ProductClick(product.id)) },
                                        onMessageIconClick = { dispatch(CompilationIntent.ProductMessageClick(product)) },
                                        onBasketIconClick = { dispatch(CompilationIntent.ProductBasketClick(product)) }
                                    ),
                                    modifier = Modifier.weight(1F)
                                )
                            }

                            if (row.size == 1) {
                                Spacer(
                                    modifier = Modifier.weight(1F)
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
@Preview(heightDp = 1300, showBackground = true)
@Composable
private fun CompilationScreenContentPreview(
    @PreviewParameter(CompilationModelProvider::class) state: CompilationModel
) {
    CompilationScreenContent(
        state = state,
        dispatch = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}

private class CompilationModelProvider: PreviewParameterProvider<CompilationModel> {
    override val values: Sequence<CompilationModel> = sequenceOf(
        CompilationModel(
            compilationPreviewPageEntities = listOf(
                CompilationPreviewPageEntity(
                    compilationId = 103,
                    id = 1,
                    position = 0,
                    compilationName = "BLV/Hotel",
                    title = "Образ 1",
                    imageUrl = ""
                ),
                CompilationPreviewPageEntity(
                    compilationId = 103,
                    id = 2,
                    position = 1,
                    compilationName = "BLV/Hotel",
                    title = "Образ 2",
                    imageUrl = ""
                )
            ),
            compilationPreviewProductEntities = listOf(
                CatalogFilterProductsEntity.Empty.copy(
                    categoryId = 103,
                    titleCategoryId = 1,
                    id = "preview-1",
                    itemId = "5558447",
                    colorId = "black",
                    brand = "SAINT LAURENT",
                    name = "Куртка из кожи",
                    price = 129_900.0,
                    priceWithoutDiscount = 189_900.0,
                    imageUrl = "",
                    actionLabels = listOf("Black Friday"),
                    lookActionPrice = 129_900.0,
                    lookActionPriceWithoutDiscount = 189_900.0,
                    lookActionName = "Black Friday",
                    lookActionDiscountPercentage = 32
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    categoryId = 103,
                    titleCategoryId = 1,
                    id = "preview-2",
                    itemId = "5558448",
                    colorId = "white",
                    brand = "BRUNELLO CUCINELLI",
                    name = "Платье миди с поясом",
                    price = 189_900.0,
                    priceWithoutDiscount = 229_900.0,
                    imageUrl = "",
                    actionLabels = listOf("Black Friday"),
                    lookActionPrice = 189_900.0,
                    lookActionPriceWithoutDiscount = 229_900.0,
                    lookActionName = "Black Friday",
                    lookActionDiscountPercentage = 17
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    categoryId = 103,
                    titleCategoryId = 2,
                    id = "preview-3",
                    itemId = "5558449",
                    colorId = "grey",
                    brand = "LORO PIANA",
                    name = "Джемпер из кашемира",
                    price = 92_500.0,
                    imageUrl = ""
                )
            )
        )
    )
}
