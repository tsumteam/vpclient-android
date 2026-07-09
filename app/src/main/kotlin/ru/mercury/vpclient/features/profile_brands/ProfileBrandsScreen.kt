@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_brands

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_brand_sheet.ProfileBrandSheet
import ru.mercury.vpclient.features.profile_brands.event.ProfileBrandsEvent
import ru.mercury.vpclient.features.profile_brands.intent.ProfileBrandsIntent
import ru.mercury.vpclient.features.profile_brands.model.ProfileBrandsModel
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.FavoriteBrandEntity
import ru.mercury.vpclient.shared.domain.usecase.FavoriteBrandEntitiesFlowUseCase.FavoriteBrandEntities
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.EmptyBox
import ru.mercury.vpclient.shared.ui.components.EmptyBoxState
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogTabData
import ru.mercury.vpclient.shared.ui.components.catalog.TabRow
import ru.mercury.vpclient.shared.ui.components.product.ProductBrandRow
import ru.mercury.vpclient.shared.ui.components.product.ProductBrandRowState
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumFavoriteBrands
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ProfileBrandsScreen(
    viewModel: ProfileBrandsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    ProfileBrandsScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    if (state.isBrandSheetVisible) {
        ProfileBrandSheet(
            categoryId = state.selectedTab.value,
            onDismiss = { viewModel.dispatch(ProfileBrandsIntent.HideBrandSheet) }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is ProfileBrandsEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun ProfileBrandsScreenContent(
    state: ProfileBrandsModel,
    dispatch: (ProfileBrandsIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val tabs = TabType.entries.map { tab ->
        CatalogTabData(
            title = stringResource(
                when (tab) {
                    TabType.WOMAN -> ClientStrings.ProfileBrandsWomanTabCaps
                    TabType.MAN -> ClientStrings.ProfileBrandsManTabCaps
                    TabType.CHILD -> ClientStrings.ProfileBrandsChildTabCaps
                }
            ),
            rootId = tab.value,
            selected = state.selectedTab == tab
        )
    }
    val selectedTabIndex = TabType.entries.indexOf(state.selectedTab)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex.coerceAtLeast(0),
        pageCount = { TabType.entries.size }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex in TabType.entries.indices && pagerState.currentPage != selectedTabIndex) {
            pagerState.scrollToPage(selectedTabIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                TabType.entries
                    .getOrNull(page)
                    ?.let { tab -> dispatch(ProfileBrandsIntent.SelectTab(tab)) }
            }
    }

    SharedScaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.ProfileFavoriteBrands),
                            style = MaterialTheme.typography.medium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(ProfileBrandsIntent.BackClick) }
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
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                TabRow(
                    tabs = tabs,
                    selectedTabIndex = pagerState.currentPage,
                    onTabClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        },
        bottomBar = {
            Button(
                onClick = { dispatch(ProfileBrandsIntent.AddFavoriteBrandsClick) },
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .placeholder(
                        visible = state.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(state.buttonTextRes),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            val entities = state.favoriteBrandEntities.pages.getOrNull(page).orEmpty()

            when {
                state.isLoading -> {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            count = 5
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
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
                entities.isEmpty() -> {
                    EmptyBox(
                        state = EmptyBoxState(
                            imageVector = VipPlatinumFavoriteBrands,
                            text = stringResource(ClientStrings.ProfileBrandsEmptyMessage)
                        ),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(
                            items = entities,
                            key = { entity -> entity.brandId }
                        ) { entity ->
                            Column(
                                modifier = Modifier.animateItem()
                            ) {
                                ProductBrandRow(
                                    state = ProductBrandRowState(
                                        entity = entity,
                                        onCloseClick = { dispatch(ProfileBrandsIntent.BrandDeleteClick(entity)) }
                                    )
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.divider
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
@Composable
private fun ProfileBrandsScreenContentPreview(
    @PreviewParameter(ProfileBrandsModelProvider::class) state: ProfileBrandsModel
) {
    ProfileBrandsScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class ProfileBrandsModelProvider: PreviewParameterProvider<ProfileBrandsModel> {

    private val favoriteBrandEntities = listOf(
        FavoriteBrandEntity(
            pairedUserId = "",
            categoryId = TabType.WOMAN.value,
            categoryName = "Женское",
            brandId = 1,
            name = "MVST",
            photoUrl = null,
            isTopBrand = false,
            isFavorite = true,
            restrictionType = null,
            position = 0
        ),
        FavoriteBrandEntity(
            pairedUserId = "",
            categoryId = TabType.WOMAN.value,
            categoryName = "Женское",
            brandId = 2,
            name = "BALMAIN",
            photoUrl = null,
            isTopBrand = true,
            isFavorite = true,
            restrictionType = null,
            position = 1
        )
    )

    override val values: Sequence<ProfileBrandsModel> = sequenceOf(
        ProfileBrandsModel(
            selectedTab = TabType.MAN,
            loadFavoriteBrandsJob = Job()
        ),
        ProfileBrandsModel(
            selectedTab = TabType.WOMAN,
            favoriteBrandEntities = FavoriteBrandEntities(
                womanEntities = favoriteBrandEntities
            )
        ),
        ProfileBrandsModel(
            selectedTab = TabType.CHILD
        )
    )
}
