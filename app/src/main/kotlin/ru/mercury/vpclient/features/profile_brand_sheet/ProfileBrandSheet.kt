@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_brand_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_brand_sheet.event.ProfileBrandSheetEvent
import ru.mercury.vpclient.features.profile_brand_sheet.intent.ProfileBrandSheetIntent
import ru.mercury.vpclient.features.profile_brand_sheet.model.ProfileBrandSheetModel
import ru.mercury.vpclient.features.profile_brand_sheet.model.ProfileBrandSheetModel.ProfileBrandSection
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.brands.BrandAlphabetScrubber
import ru.mercury.vpclient.shared.ui.components.brands.BrandAlphabetScrubberState
import ru.mercury.vpclient.shared.ui.components.brands.BrandSearchField
import ru.mercury.vpclient.shared.ui.components.brands.BrandSearchFieldState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Favorited40
import ru.mercury.vpclient.shared.ui.icons.Unfavorited40
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun ProfileBrandSheet(
    categoryId: Int,
    onDismiss: () -> Unit,
    viewModel: ProfileBrandSheetViewModel = hiltViewModel<ProfileBrandSheetViewModel, ProfileBrandSheetViewModel.Factory>(
        creationCallback = { factory -> factory.create(categoryId) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostStateError = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ProfileBrandSheetContent(
        state = state,
        dispatch = viewModel::dispatch,
        sheetState = sheetState,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is ProfileBrandSheetEvent.Dismiss -> {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            }
            is ProfileBrandSheetEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun ProfileBrandSheetContent(
    state: ProfileBrandSheetModel,
    dispatch: (ProfileBrandSheetIntent) -> Unit,
    sheetState: SheetState,
    snackbarHostStateError: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    SharedModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                dispatch(ProfileBrandSheetIntent.DismissRequest)
            }
        },
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        sheetState = sheetState
    ) {
        SharedScaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(ClientStrings.FilterBrandTitle),
                                style = MaterialTheme.typography.livretMedium18
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        dispatch(ProfileBrandSheetIntent.DismissRequest)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Close24,
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

                    BrandSearchField(
                        state = BrandSearchFieldState(
                            value = state.searchQuery,
                            onValueChange = { query -> dispatch(ProfileBrandSheetIntent.SearchQueryChange(query)) },
                            onClear = { dispatch(ProfileBrandSheetIntent.SearchQueryChange("")) },
                            onSearch = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .placeholder(
                                visible = state.isLoading,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .imePadding()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            dispatch(ProfileBrandSheetIntent.SaveClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .placeholder(
                                visible = state.isLoading,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        enabled = !state.isLoading && !state.isSaving,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        when {
                            state.isSaving -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                            else -> {
                                Text(
                                    text = stringResource(ClientStrings.ProfileBrandsSave),
                                    style = MaterialTheme.typography.medium15.copy(
                                        letterSpacing = .3.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            },
            snackbarHost = {
                SharedSnackbarHost(
                    hostState = snackbarHostStateError,
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        ) { innerPadding ->
            when {
                state.isLoading -> {
                    SharedLazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        userScrollEnabled = false
                    ) {
                        items(5) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .placeholder(
                                        visible = state.isLoading,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
                }
                state.isEmptySearchVisible -> {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(horizontal = 44.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(ClientStrings.ProfileBrandsEmptySearch),
                            style = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                else -> {
                    val lazyListState = rememberLazyListState()
                    val letterIndices = remember(state.sections) {
                        buildMap {
                            var index = 0
                            state.sections.forEach { section ->
                                put(section.letter, index)
                                index += section.catalogBrandEntities.size + 1
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        SharedLazyColumn(
                            state = lazyListState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                        ) {
                            state.sections.forEach { section ->
                                stickyHeader(
                                    key = "header_${section.letter}"
                                ) {
                                    Text(
                                        text = section.letter,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(horizontal = 16.dp)
                                            .wrapContentHeight(Alignment.CenterVertically),
                                        style = MaterialTheme.typography.livretMedium19.copy(
                                            color = MaterialTheme.colorScheme.error,
                                            lineHeight = 26.sp,
                                            letterSpacing = .2.sp
                                        )
                                    )
                                }
                                itemsIndexed(
                                    items = section.catalogBrandEntities,
                                    key = { _, entity -> entity.brandId }
                                ) { index, brand ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .clickable {
                                                focusManager.clearFocus()
                                                dispatch(ProfileBrandSheetIntent.BrandClick(brand.brandId))
                                            }
                                            .padding(start = 16.dp, end = 24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = brand.name,
                                            modifier = Modifier.weight(1F),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.regular15.copy(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                lineHeight = 19.sp,
                                                letterSpacing = .2.sp
                                            )
                                        )

                                        Icon(
                                            imageVector = when (brand.brandId) {
                                                in state.selectedBrandIds -> Favorited40
                                                else -> Unfavorited40
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                    if (index != section.catalogBrandEntities.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    }
                                }
                            }
                        }

                        if (state.isAlphabetVisible) {
                            BrandAlphabetScrubber(
                                state = BrandAlphabetScrubberState(
                                    letters = state.letters,
                                    onLetterSelect = { letter ->
                                        letterIndices[letter]?.let { itemIndex ->
                                            scope.launch {
                                                lazyListState.scrollToItem(
                                                    index = itemIndex,
                                                    scrollOffset = 1
                                                )
                                            }
                                        }
                                    }
                                ),
                                modifier = Modifier.align(Alignment.CenterEnd)
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
private fun ProfileBrandSheetPreview(
    @PreviewParameter(ProfileBrandSheetModelProvider::class) state: ProfileBrandSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileBrandSheetContent(
            state = state,
            dispatch = {},
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}

private class ProfileBrandSheetModelProvider: PreviewParameterProvider<ProfileBrandSheetModel> {
    override val values: Sequence<ProfileBrandSheetModel> = sequenceOf(
        ProfileBrandSheetModel(
            catalogBrandEntities = emptyList(),
            sections = listOf(
                ProfileBrandSection(
                    letter = "A",
                    catalogBrandEntities = listOf(
                        CatalogBrandEntity(
                            pairedUserId = "sample-user",
                            categoryId = 2,
                            categoryName = "Category",
                            brandId = 1,
                            name = "Alexander McQueen",
                            photoUrl = null,
                            isTopBrand = true,
                            isFavorite = false,
                            restrictionType = null
                        )
                    )
                ),
                ProfileBrandSection(
                    letter = "B",
                    catalogBrandEntities = listOf(
                        CatalogBrandEntity(
                            pairedUserId = "sample-user",
                            categoryId = 2,
                            categoryName = "Category",
                            brandId = 2,
                            name = "Balmain",
                            photoUrl = null,
                            isTopBrand = true,
                            isFavorite = true,
                            restrictionType = null
                        )
                    )
                )
            ),
            selectedBrandIds = setOf(2),
            initialSelectedBrandIds = setOf(2)
        ),
        ProfileBrandSheetModel(
            loadBrandsJob = Job()
        ),
        ProfileBrandSheetModel(
            catalogBrandEntities = emptyList(),
            sections = emptyList(),
            searchQuery = "Missing brand"
        )
    )
}
