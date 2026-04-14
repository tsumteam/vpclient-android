package ru.mercury.vpclient.features.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.details.intent.DetailsIntent
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.IndicatorIconButton
import ru.mercury.vpclient.shared.ui.components.details.DetailsColorImageSelector
import ru.mercury.vpclient.shared.ui.components.details.DetailsCompleteSetSection
import ru.mercury.vpclient.shared.ui.components.details.DetailsFieldRow
import ru.mercury.vpclient.shared.ui.components.details.DetailsOutfitButton
import ru.mercury.vpclient.shared.ui.components.details.DetailsPagerIndicator
import ru.mercury.vpclient.shared.ui.components.details.DetailsProductInfoBox
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeSelector
import ru.mercury.vpclient.shared.ui.components.details.DetailsWearWithSection
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientOutlinedButton
import ru.mercury.vpclient.shared.ui.icons.Basket24
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.FittingShirt24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.DetailsModelProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.surface4

// fixme

@Composable
fun DetailsScreen(
    route: DetailsRoute,
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    DetailsScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun DetailsScreenContent(
    state: DetailsModel,
    dispatch: (DetailsIntent) -> Unit
) {
    val pagerImages = remember(state.productEntity.colorImageUrls, state.productEntity.urlItemVideo) {
        state.productEntity.colorImageUrls + listOfNotNull(state.productEntity.urlItemVideo)
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { if (pagerImages.isEmpty()) 0 else Int.MAX_VALUE }
    )

    LaunchedEffect(pagerImages.size) {
        if (pagerImages.isNotEmpty()) {
            val mid = Int.MAX_VALUE / 2
            pagerState.scrollToPage(mid - mid % pagerImages.size)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(DetailsIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IndicatorIconButton(
                        icon = FittingShirt24,
                        showIndicator = true,
                        onClick = {}
                    )

                    IndicatorIconButton(
                        icon = Basket24,
                        showIndicator = true,
                        onClick = {}
                    )

                    IndicatorIconButton(
                        icon = Chat24,
                        showIndicator = true,
                        onClick = {},
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            ClientButton(
                onClick = {},
                text = stringResource(ClientStrings.DetailsAddToBasket),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .placeholder(
                        visible = state.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colorScheme.surface4,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        when {
            state.isLoading -> {
                ClientLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(top = innerPadding.calculateTopPadding()),
                    userScrollEnabled = false
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 31.dp, top = 16.dp, end = 31.dp)
                                .aspectRatio(3f / 4f)
                                .clip(RoundedCornerShape(4.dp))
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surface4
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
                                            color = MaterialTheme.colorScheme.surface4,
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
                                        color = MaterialTheme.colorScheme.surface4,
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
                                            color = MaterialTheme.colorScheme.surface4,
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
                                            color = MaterialTheme.colorScheme.surface4,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                ClientLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding().plus(120.dp)
                    )
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
                                userScrollEnabled = pagerImages.size > 1
                            ) { page ->
                                ClientAsyncImage(
                                    imageUrl = pagerImages[page % pagerImages.size],
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            DetailsOutfitButton(
                                onClick = {},
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 16.dp, bottom = 39.dp)
                            )
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
                            pageCount = pagerImages.size,
                            pageIndexMapping = { it % pagerImages.size },
                            showVideoIcon = state.hasVideo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                    }
                    item {
                        DetailsProductInfoBox(
                            productEntity = state.productEntity,
                            onMessageClick = { dispatch(DetailsIntent.MessageClick) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (state.isSizePickerVisible) {
                        item {
                            DetailsSizeSelector(
                                state = state.sizePickerState,
                                onSizeClick = { dispatch(DetailsIntent.SizeClick(it)) },
                                onSizeTableClick = { dispatch(DetailsIntent.SizeTableClick) }
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
                                colorImageUrls = state.productEntity.otherColorImageUrls
                            )
                        }
                    }
                    if (state.isDescriptionTextVisible) {
                        item {
                            Text(
                                text = state.descriptionText.orEmpty(), // fixme
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
                                field = field,
                                modifier = Modifier.padding(start = 16.dp, top = 24.dp)
                            )
                        }
                        if (index != state.detailFields.lastIndex) {
                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.surface4
                                )
                            }
                        }
                    }
                    if (state.isWearWithBoxVisible) {
                        item {
                            DetailsWearWithSection(
                                products = state.wearWithProducts,
                                onProductClick = { id -> dispatch(DetailsIntent.ProductClick(id)) }
                            )
                        }
                    }
                    if (state.isCompleteSetBoxVisible) {
                        item {
                            DetailsCompleteSetSection(
                                products = state.completeSetProducts,
                                onProductClick = { id -> dispatch(DetailsIntent.ProductClick(id)) }
                            )
                        }
                    }
                    state.productEntity.breadcrumbs.forEachIndexed { index, breadcrumb ->
                        item {
                            ClientOutlinedButton(
                                onClick = {},
                                text = breadcrumb,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        top = if (index == 0) 24.dp else 16.dp,
                                        end = 16.dp
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}


@FontScalePreviews
@Composable
private fun DetailsScreenContentPreview(
    @PreviewParameter(DetailsModelProvider::class) state: DetailsModel
) {
    ClientTheme {
        DetailsScreenContent(
            state = state,
            dispatch = {}
        )
    }
}
