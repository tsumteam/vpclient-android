@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.entity.BrandFilterValue
import ru.mercury.vpclient.shared.ktx.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.ktx.requireQuantity
import ru.mercury.vpclient.shared.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientDragHandle
import ru.mercury.vpclient.shared.ui.components.filters.FilterChip
import ru.mercury.vpclient.shared.ui.components.filters.FilterSelectableRow
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium16
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular15
import ru.mercury.vpclient.shared.ui.theme.regular16
import ru.mercury.vpclient.shared.ui.theme.secondary5
import ru.mercury.vpclient.shared.ui.theme.secondary6
import ru.mercury.vpclient.shared.ui.theme.surface3
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.intent.FilterBrandIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.model.FilterBrandSheetState

// fixme

@Composable
fun FilterBrandSheet(
    state: FilterBrandSheetState,
    dispatch: (FilterBrandIntent) -> Unit
) {
    FilterBrandSheetContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun FilterBrandSheetContent(
    state: FilterBrandSheetState,
    dispatch: (FilterBrandIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val hapticFeedback = LocalHapticFeedback.current
    var searchText by remember { mutableStateOf("") }
    val isKeyboardOpen = WindowInsets.isImeVisible
    val animatingIds = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(state.selectedIds) {
        val currentIds = state.selectedIds
        currentIds.forEach { id -> animatingIds[id] = true }
        val toRemove = animatingIds.keys.filter { it !in currentIds }
        toRemove.forEach { id -> animatingIds[id] = false }
        if (toRemove.isNotEmpty()) {
            delay(250)
            toRemove.forEach { animatingIds.remove(it) }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { dispatch(FilterBrandIntent.HideFilterBrandDialog) },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { ClientDragHandle() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            dispatch(FilterBrandIntent.HideFilterBrandDialog)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Close24,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = stringResource(ClientStrings.FilterBrandTitle),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp, vertical = 0.dp),
                    style = MaterialTheme.typography.livretMedium19.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )

                ClientAnimatedVisibility(
                    visible = state.selectedIds.isNotEmpty(),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(
                        onClick = {
                            focusManager.clearFocus()
                            dispatch(FilterBrandIntent.ResetFilterBrandValues)
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CommonReset),
                            style = MaterialTheme.typography.medium16.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }

            BrandSearchField(
                value = searchText,
                onValueChange = { searchText = it },
                onClear = { searchText = "" },
                onSearch = { focusManager.clearFocus() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            if (animatingIds.isNotEmpty()) {
                val displayedBrands = state.brands.filter { it.id in animatingIds }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 157.dp)
                        .padding(top = 16.dp)
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        displayedBrands.forEach { brand ->
                            key(brand.id) {
                                AnimatedVisibility(
                                    visible = animatingIds[brand.id] == true,
                                    enter = fadeIn(tween(200)) + expandHorizontally(tween(200)),
                                    exit = fadeOut(tween(200)) + shrinkHorizontally(tween(200))
                                ) {
                                    FilterChip(
                                        text = brand.label,
                                        selected = true,
                                        enabled = true,
                                        onClick = {
                                            focusManager.clearFocus()
                                            dispatch(FilterBrandIntent.ToggleFilterBrandValue(brand.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                val favoriteBrands = state.brands.filter { it.isFavorite }
                val topBrands = state.brands.filter { it.isTopBrand && !it.isFavorite }
                val filteredBrands = when {
                    searchText.isBlank() -> state.brands
                    else -> state.brands.filter { it.label.contains(searchText, ignoreCase = true) }
                }
                val brandsByLetter = filteredBrands.groupBy { brand ->
                    val firstChar = brand.label.firstOrNull()?.lowercaseChar() ?: '#'
                    when {
                        firstChar in 'a'..'z' -> firstChar.uppercaseChar().toString()
                        else -> "#"
                    }
                }.entries.sortedWith(
                    Comparator { a, b ->
                        when {
                            a.key == "#" && b.key != "#" -> 1
                            a.key != "#" && b.key == "#" -> -1
                            else -> a.key.compareTo(b.key)
                        }
                    }
                )

                val lazyListState = rememberLazyListState()

                val letterIndices = remember(brandsByLetter, favoriteBrands, topBrands, searchText) {
                    val map = mutableMapOf<String, Int>()
                    var index = 0
                    if (searchText.isBlank()) {
                        if (favoriteBrands.isNotEmpty()) index += 2
                        if (topBrands.isNotEmpty()) index += 2
                        index += 1
                    }
                    brandsByLetter.forEach { (letter, brands) ->
                        map[letter] = index
                        index += 1 + brands.size
                    }
                    map
                }

                val allBrandsHeaderIndex = remember(favoriteBrands, topBrands) {
                    var index = 0
                    if (favoriteBrands.isNotEmpty()) index += 2
                    if (topBrands.isNotEmpty()) index += 2
                    index
                }

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 72.dp)
                ) {
                    if (searchText.isBlank()) {
                        if (favoriteBrands.isNotEmpty()) {
                            item {
                                BrandSectionHeader(
                                    title = stringResource(ClientStrings.FilterBrandFavoritesHeader),
                                    showSelectAll = !favoriteBrands.all { it.id in state.selectedIds },
                                    onSelectAll = {
                                        focusManager.clearFocus()
                                        dispatch(FilterBrandIntent.SelectAllBrands(favoriteBrands.map { it.id }.toSet()))
                                    }
                                )
                            }
                            item {
                                BrandChipsGrid(
                                    brands = favoriteBrands,
                                    onToggle = {
                                        focusManager.clearFocus()
                                        dispatch(FilterBrandIntent.ToggleFilterBrandValue(it))
                                    },
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                            }
                        }

                        if (topBrands.isNotEmpty()) {
                            item {
                                BrandSectionHeader(
                                    title = stringResource(ClientStrings.FilterBrandTopHeader),
                                    showSelectAll = !topBrands.all { it.id in state.selectedIds },
                                    onSelectAll = {
                                        focusManager.clearFocus()
                                        dispatch(FilterBrandIntent.SelectAllBrands(topBrands.map { it.id }.toSet()))
                                    }
                                )
                            }
                            item {
                                BrandChipsGrid(
                                    brands = topBrands,
                                    onToggle = {
                                        focusManager.clearFocus()
                                        dispatch(FilterBrandIntent.ToggleFilterBrandValue(it))
                                    },
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                            }
                        }

                        item {
                            BrandSectionHeader(
                                title = stringResource(ClientStrings.FilterBrandAllHeader),
                                showSelectAll = false,
                                onSelectAll = {}
                            )
                        }
                    }

                    if (filteredBrands.isEmpty() && searchText.isNotBlank()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.FilterBrandEmptySearch),
                                    style = MaterialTheme.typography.regular15.copy(
                                        color = MaterialTheme.colorScheme.secondary6,
                                        lineHeight = 19.sp,
                                        letterSpacing = .2.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    } else {
                        brandsByLetter.forEach { (letter, brands) ->
                            stickyHeader(key = "header_$letter") {
                                LetterHeader(letter = letter)
                            }
                            itemsIndexed(
                                items = brands,
                                key = { _, brand -> brand.id }
                            ) { index, brand ->
                                FilterSelectableRow(
                                    text = brand.label,
                                    selected = brand.id in state.selectedIds,
                                    onClick = {
                                        focusManager.clearFocus()
                                        dispatch(FilterBrandIntent.ToggleFilterBrandValue(brand.id))
                                    }
                                )
                                if (index != brands.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 48.dp),
                                        color = MaterialTheme.colorScheme.secondary5
                                    )
                                }
                            }
                        }
                    }
                }

                val showScrubber = searchText.isBlank() &&
                    brandsByLetter.isNotEmpty() &&
                    lazyListState.firstVisibleItemIndex >= allBrandsHeaderIndex

                val letters = brandsByLetter.map { it.key }
                var scrubberHeight by remember { mutableFloatStateOf(0f) }

                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                ClientAnimatedVisibility(
                    visible = showScrubber,
                    enter = fadeIn(tween(200)) + slideInHorizontally(tween(200)) { it },
                    exit = fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it }
                ) {
                    Column(
                        modifier = Modifier
                            .width(18.dp)
                            .onSizeChanged { scrubberHeight = it.height.toFloat() }
                            .pointerInput(letters, letterIndices) {
                                awaitEachGesture {
                                    var currentLetter: String? = null
                                    fun scrollToY(y: Float) {
                                        if (scrubberHeight == 0f || letters.isEmpty()) return
                                        val idx = (y / scrubberHeight * letters.size)
                                            .toInt()
                                            .coerceIn(0, letters.lastIndex)
                                        val newLetter = letters[idx]
                                        val itemIndex = letterIndices[newLetter] ?: return
                                        if (newLetter != currentLetter) {
                                            currentLetter = newLetter
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        }
                                        scope.launch { lazyListState.scrollToItem(itemIndex, scrollOffset = 1) }
                                    }
                                    val down = awaitFirstDown(requireUnconsumed = false)
                                    scrollToY(down.position.y)
                                    drag(down.id) { change ->
                                        scrollToY(change.position.y)
                                        change.consume()
                                    }
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        letters.forEach { letter ->
                            Text(
                                text = letter,
                                style = MaterialTheme.typography.regular12.copy(color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                            )
                        }
                    }
                }
                }

                if (!isKeyboardOpen) {
                    ClientButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                dispatch(FilterBrandIntent.ConfirmFilterBrandValues)
                            }
                        },
                        text = pluralStringResource(
                            ClientStrings.FilterShowProductsQuantity,
                            state.quantityEntity.requireQuantity,
                            state.quantityEntity.quantityWithThousandsSeparator
                        ),
                        loading = state.isProductsQuantityLoading,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BrandSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .height(56.dp)
            .background(
                color = MaterialTheme.colorScheme.surface3,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Search24,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(24.dp),
            tint = MaterialTheme.colorScheme.secondary6
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 56.dp,
                    end = if (value.isEmpty()) 16.dp else 48.dp
                ),
            singleLine = true,
            textStyle = MaterialTheme.typography.regular16.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp,
                letterSpacing = .2.sp
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                onSearch()
            }),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(ClientStrings.FilterBrandSearchPlaceholder),
                            style = MaterialTheme.typography.regular16.copy(
                                color = MaterialTheme.colorScheme.secondary6,
                                lineHeight = 20.sp,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        ClientAnimatedVisibility(
            visible = value.isNotEmpty(),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(onClick = onClear) {
                Icon(
                    imageVector = Close24,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary6
                )
            }
        }
    }
}

@Composable
private fun BrandSectionHeader(
    title: String,
    showSelectAll: Boolean,
    onSelectAll: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.livretMedium19.copy(
                color = MaterialTheme.colorScheme.error,
                lineHeight = 26.sp,
                letterSpacing = .2.sp
            )
        )

        ClientAnimatedVisibility(
            visible = showSelectAll,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            TextButton(
                onClick = onSelectAll,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(ClientStrings.FilterBrandSelectAll),
                    style = MaterialTheme.typography.medium16.copy(
                        color = MaterialTheme.colorScheme.error,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun LetterHeader(letter: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.livretMedium19.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 26.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@Composable
private fun BrandChipsGrid(
    brands: List<BrandFilterValue>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        brands.chunked(3).forEach { rowBrands ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowBrands.forEach { brand ->
                    BrandChip(
                        brand = brand,
                        onClick = { onToggle(brand.id) },
                        modifier = Modifier.weight(1F)
                    )
                }
                repeat(3 - rowBrands.size) {
                    Spacer(modifier = Modifier.weight(1F))
                }
            }
        }
    }
}

@Composable
private fun BrandChip(
    brand: BrandFilterValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (brand.labelPhotoUrl != null) {
            AsyncImage(
                model = brand.labelPhotoUrl,
                contentDescription = brand.label,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        } else {
            Text(
                text = brand.label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular15.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
