@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_view_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mercury.vpclient.features.profile_view_history.intent.ProfileViewHistoryIntent
import ru.mercury.vpclient.features.profile_view_history.model.ProfileViewHistoryModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.PagingFailureBox
import ru.mercury.vpclient.shared.ui.components.PagingLoadingBox
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.product.ProductCard
import ru.mercury.vpclient.shared.ui.components.product.ProductCardState
import ru.mercury.vpclient.shared.ui.components.filters.FilterProductCardPlaceholder
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.isPagingFailure
import ru.mercury.vpclient.shared.ui.ktx.isPagingLoading
import ru.mercury.vpclient.shared.ui.ktx.isRefreshFailure
import ru.mercury.vpclient.shared.ui.ktx.isRefreshLoading
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ProfileViewHistoryScreen(
    viewModel: ProfileViewHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pagingItems = viewModel.productsPagingFlow.collectAsLazyPagingItems()

    ProfileViewHistoryScreenContent(
        state = state,
        pagingItems = pagingItems,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ProfileViewHistoryScreenContent(
    state: ProfileViewHistoryModel,
    pagingItems: LazyPagingItems<CatalogFilterProductsEntity>,
    dispatch: (ProfileViewHistoryIntent) -> Unit
) {
    val isInitialLoading = pagingItems.isRefreshLoading && pagingItems.itemCount == 0

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfileRecentlyViewed),
                        style = MaterialTheme.typography.medium18
                    )
                },
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { dispatch(ProfileViewHistoryIntent.BackClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = { dispatch(ProfileViewHistoryIntent.SearchClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.isCartBadgeVisible,
                        onClick = { dispatch(ProfileViewHistoryIntent.CartClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isInitialLoading -> {
                    ProfileViewHistoryLoadingContent(
                        contentPadding = innerPadding + PaddingValues(
                            start = 4.dp,
                            top = 4.dp,
                            end = 4.dp,
                            bottom = 16.dp
                        ),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                pagingItems.isRefreshFailure -> {
                    PagingFailureBox(
                        onClick = pagingItems::retry,
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .fillMaxSize()
                    )
                }
                else -> {
                    SharedPullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = pagingItems::refresh,
                        modifier = Modifier.fillMaxSize(),
                        indicator = {}
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            contentPadding = innerPadding + PaddingValues(
                                start = 4.dp,
                                top = 4.dp,
                                end = 4.dp,
                                bottom = 4.dp
                            )
                        ) {
                            items(
                                count = pagingItems.itemCount,
                                key = pagingItems.itemKey(),
                                contentType = pagingItems.itemContentType()
                            ) { index ->
                                val entity = pagingItems[index]
                                if (entity != null) {
                                    ProductCard(
                                        state = ProductCardState(
                                            entity = entity,
                                            isInBasket = state.isProductInBasket(entity),
                                            onClick = { dispatch(ProfileViewHistoryIntent.ProductClick(entity.id)) },
                                            onBasketIconClick = {
                                                dispatch(ProfileViewHistoryIntent.ProductBasketClick(entity))
                                            }
                                        )
                                    )
                                }
                            }

                            when {
                                pagingItems.isPagingLoading -> {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        PagingLoadingBox(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                        )
                                    }
                                }
                                pagingItems.isPagingFailure -> {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        PagingFailureBox(
                                            onClick = pagingItems::retry,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileViewHistoryLoadingContent(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = contentPadding,
        userScrollEnabled = false
    ) {
        items(
            count = 4
        ) {
            FilterProductCardPlaceholder()
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileViewHistoryScreenContentPreview() {
    val products = listOf(
        CatalogFilterProductsEntity.Empty.copy(
            id = "preview-1",
            itemId = "item-1",
            colorId = "black",
            brand = "SAINT LAURENT",
            imageUrl = "",
            imageUrls = listOf(""),
            position = 0
        ),
        CatalogFilterProductsEntity.Empty.copy(
            id = "preview-2",
            itemId = "item-2",
            colorId = "white",
            brand = "BRUNELLO CUCINELLI",
            imageUrl = "",
            imageUrls = listOf(""),
            position = 1
        ),
        CatalogFilterProductsEntity.Empty.copy(
            id = "preview-3",
            itemId = "item-3",
            colorId = "blue",
            brand = "TOM FORD",
            imageUrl = "",
            imageUrls = listOf(""),
            position = 2
        ),
        CatalogFilterProductsEntity.Empty.copy(
            id = "preview-4",
            itemId = "item-4",
            colorId = "beige",
            brand = "LORO PIANA",
            imageUrl = "",
            imageUrls = listOf(""),
            position = 3
        )
    )
    val pagingItems = remember {
        MutableStateFlow(PagingData.from(products))
    }.collectAsLazyPagingItems()

    ProfileViewHistoryScreenContent(
        state = ProfileViewHistoryModel(),
        pagingItems = pagingItems,
        dispatch = {}
    )
}
