@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.gift_card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
import ru.mercury.vpclient.features.gift_card.event.GiftCardEvent
import ru.mercury.vpclient.features.gift_card.intent.GiftCardIntent
import ru.mercury.vpclient.features.gift_card.model.GiftCardModel
import ru.mercury.vpclient.features.gift_card_terms_sheet.GiftCardTermsSheet
import ru.mercury.vpclient.features.gift_card_terms_sheet.intent.GiftCardTermsIntent
import ru.mercury.vpclient.features.gift_card_terms_sheet.model.GiftCardTermsModel
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardTemplateEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.transformation.GiftCardAmountOutputTransformation

@Composable
fun GiftCardScreen(
    viewModel: GiftCardViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    GiftCardScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is GiftCardEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }

    if (state.isTermsVisible) {
        GiftCardTermsSheet(
            state = GiftCardTermsModel(
                text = state.selectedTemplate.termOfUse
            ),
            dispatch = { intent ->
                when (intent) {
                    is GiftCardTermsIntent.DismissRequest -> {
                        viewModel.dispatch(GiftCardIntent.TermsDismiss)
                    }
                }
            }
        )
    }
}

@Composable
private fun GiftCardScreenContent(
    state: GiftCardModel,
    dispatch: (GiftCardIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val amountOutputTransformation = remember { GiftCardAmountOutputTransformation() }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = state.selectedTemplateIndex,
        pageCount = { state.templates.size }
    )

    LaunchedEffect(state.selectedTemplateIndex, state.templates.size) {
        when {
            state.selectedTemplateIndex in state.templates.indices &&
                pagerState.currentPage != state.selectedTemplateIndex -> {
                pagerState.scrollToPage(state.selectedTemplateIndex)
            }
        }
    }

    LaunchedEffect(pagerState, state.templates.size) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                when (page) {
                    in state.templates.indices -> dispatch(GiftCardIntent.SelectTemplate(page))
                }
            }
    }

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(GiftCardIntent.BackClick) },
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
                    FittingIconButton(
                        text = "",
                        showBadge = state.isFittingBadgeVisible,
                        onClick = { dispatch(GiftCardIntent.FittingClick) }
                    )

                    CartIconButton(
                        text = "",
                        showBadge = state.isCartBadgeVisible,
                        onClick = { dispatch(GiftCardIntent.CartClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                containerColor = MaterialTheme.colorScheme.error
            )
        },
        floatingActionButton = {
            Button(
                onClick = { dispatch(GiftCardIntent.BuyClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .placeholder(
                        visible = state.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ),
                enabled = state.isBuyEnabled,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.GiftCardBuy),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(top = 8.dp, bottom = 92.dp),
            userScrollEnabled = !state.isLoading
        ) {
            when {
                state.isLoading -> {
                    item {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val cardWidth = maxWidth * .76F

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardWidth / 1.6F),
                                contentPadding = PaddingValues(horizontal = (maxWidth - cardWidth) / 2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                userScrollEnabled = false
                            ) {
                                items(
                                    count = 2
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .size(width = cardWidth, height = cardWidth / 1.6F)
                                            .placeholder(shape = RoundedCornerShape(10.dp))
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(width = 190.dp, height = 24.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )

                            Spacer(
                                modifier = Modifier
                                    .size(width = 120.dp, height = 18.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(width = 48.dp, height = 14.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .placeholder(shape = RoundedCornerShape(8.dp))
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(.85F)
                                    .height(14.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )
                        }
                    }
                    item {
                        LazyRow(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            userScrollEnabled = false
                        ) {
                            items(
                                count = 4
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(width = 88.dp, height = 34.dp)
                                        .placeholder(shape = RoundedCornerShape(10.dp))
                                )
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(.7F)
                                    .height(16.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                                .size(width = 164.dp, height = 18.dp)
                                .placeholder(shape = RoundedCornerShape(4.dp))
                        )
                    }
                }
                else -> {
                    item {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val cardWidth = maxWidth * .76F

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardWidth / 1.6F),
                                contentPadding = PaddingValues(horizontal = (maxWidth - cardWidth) / 2),
                                pageSpacing = 8.dp,
                                pageSize = PageSize.Fixed(cardWidth)
                            ) { page ->
                                ClientAsyncImage(
                                    imageUrl = state.templates[page].photoUrl,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickableWithoutRipple(
                                            enabled = page != pagerState.currentPage,
                                            onClick = { scope.launch { pagerState.animateScrollToPage(page) } }
                                        )
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = stringResource(ClientStrings.GiftCardTitle),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(44.dp)
                                .wrapContentHeight(Alignment.CenterVertically),
                            style = MaterialTheme.typography.livretMedium18.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 26.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    item {
                        Text(
                            text = state.selectedTemplate.name,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    item {
                        Text(
                            text = stringResource(ClientStrings.GiftCardAmount),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(40.dp)
                                .wrapContentHeight(Alignment.CenterVertically),
                            style = MaterialTheme.typography.regular12.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                    item {
                        ClientTextField(
                            value = state.amountText,
                            accepted = !state.isAmountErrorVisible,
                            onValueChange = { value -> dispatch(GiftCardIntent.AmountChange(value)) },
                            placeholder = "",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            outputTransformation = amountOutputTransformation
                        )
                    }
                    item {
                        Text(
                            text = stringResource(
                                ClientStrings.GiftCardAmountRange,
                                state.minAmountText,
                                state.maxAmountText
                            ),
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                            style = MaterialTheme.typography.regular12.copy(
                                color = when {
                                    state.isAmountErrorVisible -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                lineHeight = 16.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                    item {
                        LazyRow(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.presetAmounts,
                                key = { item -> item.amount }
                            ) { item ->
                                OutlinedButton(
                                    onClick = { dispatch(GiftCardIntent.SelectAmount(item.amount)) },
                                    modifier = Modifier.height(34.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                ) {
                                    Text(
                                        text = item.text,
                                        style = MaterialTheme.typography.regular14.copy(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            lineHeight = 20.sp,
                                            letterSpacing = .2.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            text = state.selectedTemplate.description,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                    item {
                        TextButton(
                            onClick = { dispatch(GiftCardIntent.TermsClick) },
                            modifier = Modifier
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                                .height(38.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(ClientStrings.GiftCardTerms),
                                style = MaterialTheme.typography.medium15.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    lineHeight = 15.sp,
                                    letterSpacing = .3.sp
                                )
                            )
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
private fun GiftCardScreenContentPreview(
    @PreviewParameter(GiftCardModelPreviewParameterProvider::class) state: GiftCardModel
) {
    GiftCardScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class GiftCardModelPreviewParameterProvider: PreviewParameterProvider<GiftCardModel> {
    override val values: Sequence<GiftCardModel> = sequenceOf(
        GiftCardModel(
            loadGiftCardJob = Job()
        ),
        GiftCardModel(
            giftCardEntity = GiftCardEntity.Empty.copy(
                id = 1,
                type = GiftCardType.VIRTUAL,
                minAmount = 3_000,
                maxAmount = 1_000_000,
                defaultAmount = 50_000,
                presetAmounts = listOf(50_000, 100_000, 200_000, 300_000),
                templates = listOf(
                    GiftCardTemplateEntity.Empty.copy(
                        id = 1,
                        name = "Классическая черная карта",
                        description = "Подарочный дизайн карты VIP Platinum идеально подойдет в качестве подарка.",
                        termOfUse = "Условия использования подарочной карты"
                    ),
                    GiftCardTemplateEntity.Empty.copy(
                        id = 2,
                        name = "Бежевая карта"
                    )
                ),
            ),
            amountText = "50000",
            isBuyEnabled = true
        ),
        GiftCardModel(
            giftCardEntity = GiftCardEntity.Empty.copy(
                id = 1,
                type = GiftCardType.VIRTUAL,
                minAmount = 3_000,
                maxAmount = 1_000_000,
                defaultAmount = 50_000,
                presetAmounts = listOf(50_000, 100_000, 200_000, 300_000),
                templates = listOf(
                    GiftCardTemplateEntity.Empty.copy(
                        id = 1,
                        name = "Классическая черная карта"
                    ),
                    GiftCardTemplateEntity.Empty.copy(
                        id = 2,
                        name = "Бежевая карта",
                        description = "Подарочный дизайн карты VIP Platinum идеально подойдет в качестве подарка.",
                        termOfUse = "Условия использования подарочной карты"
                    )
                )
            ),
            selectedTemplateIndex = 1,
            amountText = "50000",
            isBuyEnabled = true
        ),
        GiftCardModel(
            giftCardEntity = GiftCardEntity.Empty.copy(
                id = 1,
                type = GiftCardType.VIRTUAL,
                minAmount = 3_000,
                maxAmount = 1_000_000,
                defaultAmount = 50_000,
                presetAmounts = listOf(50_000, 100_000, 200_000, 300_000),
                templates = listOf(
                    GiftCardTemplateEntity.Empty.copy(
                        id = 1,
                        name = "Классическая черная карта"
                    ),
                    GiftCardTemplateEntity.Empty.copy(
                        id = 2,
                        name = "Бежевая карта",
                        description = "Подарочный дизайн карты VIP Platinum идеально подойдет в качестве подарка.",
                        termOfUse = "Условия использования подарочной карты"
                    ),
                    GiftCardTemplateEntity.Empty.copy(
                        id = 3,
                        name = "Красная карта"
                    )
                )
            ),
            selectedTemplateIndex = 1,
            amountText = "50000",
            isBuyEnabled = true
        ),
        GiftCardModel(
            giftCardEntity = GiftCardEntity.Empty.copy(
                id = 1,
                type = GiftCardType.VIRTUAL,
                minAmount = 3_000,
                maxAmount = 1_000_000,
                defaultAmount = 50_000,
                presetAmounts = listOf(50_000, 100_000, 200_000, 300_000),
                templates = listOf(
                    GiftCardTemplateEntity.Empty.copy(
                        id = 1,
                        name = "Классическая черная карта",
                        description = "Подарочный дизайн карты VIP Platinum идеально подойдет в качестве подарка.",
                        termOfUse = "Условия использования подарочной карты"
                    ),
                    GiftCardTemplateEntity.Empty.copy(
                        id = 2,
                        name = "Бежевая карта"
                    )
                )
            ),
            amountText = "1000",
            isAmountErrorVisible = true,
            isBuyEnabled = false
        )
    )
}
