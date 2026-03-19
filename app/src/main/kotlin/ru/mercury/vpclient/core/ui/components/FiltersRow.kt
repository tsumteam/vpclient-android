package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.entity.FilterChip
import ru.mercury.vpclient.core.entity.FilterRibbonData
import ru.mercury.vpclient.core.ktx.isEmpty
import ru.mercury.vpclient.core.ktx.isNotEmpty
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.shimmer
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.FilterRibbonDataProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.surface4

// fixme

private const val SORT_CHIP_KEY = "sort_chip"
private const val RESET_CHIP_KEY = "reset_chip"

@Composable
fun FiltersRow(
    filterRibbonData: FilterRibbonData,
    sortSelected: Boolean,
    selectedFilterValueChips: List<FilterChip>,
    enabled: Boolean,
    onSortClick: () -> Unit,
    onFilterChipClick: (String) -> Unit,
    onFilterValueChipClick: (chipId: String) -> Unit,
    onReset: () -> Unit
) {
    val topRowState = rememberLazyListState()
    val bottomRowState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var pendingScrollChipId by remember { mutableStateOf<String?>(null) }
    val selectedFilterValueChipIds = selectedFilterValueChips.map(FilterChip::id).toSet()
    val topFilterChipIds = filterRibbonData.topFilterChips.map(FilterChip::id)
    val bottomFilterChipIds = filterRibbonData.bottomFilterChips.map(FilterChip::id)
    val filterChipIds = topFilterChipIds
        .plus(bottomFilterChipIds)
        .sortedByDescending(String::length)
    val selectedFilterValueChipsByParentChipId = selectedFilterValueChips
        .mapNotNull { chip ->
            val parentChipId = chip.parentFilterChipId(filterChipIds) ?: return@mapNotNull null
            parentChipId to chip
        }
        .groupBy(
            keySelector = Pair<String, FilterChip>::first,
            valueTransform = Pair<String, FilterChip>::second
        )
    val selectedFilterValueChipsWithParentIds = selectedFilterValueChipsByParentChipId.values.flatten().map(FilterChip::id).toSet()
    val selectedStandaloneFilterValueChips = selectedFilterValueChips.filterNot { chip ->
        chip.id in selectedFilterValueChipsWithParentIds
    }
    val selectedTopFilterValueChips = buildList {
        addAll(filterRibbonData.topFilterValueChips.filter { chip -> chip.id in selectedFilterValueChipIds })
        addAll(selectedFilterValueChips.filter { selectedChip ->
            selectedChip.parentFilterChipId(topFilterChipIds) != null &&
                none { chip -> chip.id == selectedChip.id }
        })
        addAll(selectedStandaloneFilterValueChips.filterNot { selectedChip ->
            any { chip -> chip.id == selectedChip.id }
        })
    }
    val unselectedTopFilterValueChips = filterRibbonData.topFilterValueChips.filterNot { chip -> chip.id in selectedFilterValueChipIds }
    val bottomFilterChipsById = filterRibbonData.bottomFilterChips.associateBy(FilterChip::id)
    val selectedBottomFilterChips = selectedFilterValueChipsByParentChipId.keys.mapNotNull { chipId ->
        bottomFilterChipsById[chipId]
    }
    val unselectedBottomFilterChips = filterRibbonData.bottomFilterChips.filterNot { chip ->
        selectedFilterValueChipsByParentChipId[chip.id]?.isNotEmpty() == true
    }
    val bottomFilterChips = selectedBottomFilterChips + unselectedBottomFilterChips
    val topRowChipIds = buildList {
        add(SORT_CHIP_KEY)
        addAll(filterRibbonData.topFilterChips.map(FilterChip::id))
        addAll(selectedTopFilterValueChips.map(FilterChip::id))
        addAll(unselectedTopFilterValueChips.map(FilterChip::id))
        if (sortSelected || selectedFilterValueChips.isNotEmpty()) {
            add(RESET_CHIP_KEY)
        }
    }
    val bottomRowChipIds = buildList {
        bottomFilterChips.forEach { chip ->
            add(chip.id)
            addAll(selectedFilterValueChipsByParentChipId[chip.id].orEmpty().map(FilterChip::id))
        }
    }

    LaunchedEffect(topRowChipIds, bottomRowChipIds, pendingScrollChipId) {
        val chipId = pendingScrollChipId ?: return@LaunchedEffect
        if (chipId !in selectedFilterValueChipIds) {
            return@LaunchedEffect
        }
        val topChipIndex = topRowChipIds.indexOf(chipId)
        when {
            topChipIndex >= 0 -> topRowState.animateScrollToItem(topChipIndex)
            else -> {
                val bottomChipIndex = bottomRowChipIds.indexOf(chipId)
                if (bottomChipIndex >= 0) {
                    bottomRowState.animateScrollToItem(bottomChipIndex)
                }
            }
        }
        pendingScrollChipId = null
    }

    Column {
        LazyRow(
            state = topRowState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = filterRibbonData.isNotEmpty && enabled
        ) {
            when {
                filterRibbonData.isEmpty -> {
                    item {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surface4,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                    }
                    items(
                        items = listOf(112.dp, 96.dp, 104.dp, 112.dp)
                    ) { width ->
                        Box(
                            modifier = Modifier
                                .width(width)
                                .height(40.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surface4,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                    }
                }
                else -> {
                    item {
                        SortChip(
                            selected = sortSelected,
                            enabled = enabled,
                            onClick = onSortClick
                        )
                    }
                    filterRibbonData.topFilterChips.forEach { chipData ->
                        item(
                            key = chipData.id
                        ) {
                            TextChip(
                                text = chipData.label,
                                selected = selectedFilterValueChipIds.any { chipId -> chipId.startsWith("${chipData.id}_") },
                                enabled = enabled,
                                onClick = { onFilterChipClick(chipData.id) }
                            )
                        }
                    }
                    items(
                        items = selectedTopFilterValueChips,
                        key = { chipData -> chipData.id }
                    ) { chipData ->
                        FilterChip(
                            text = chipData.label,
                            selected = true,
                            enabled = enabled,
                            onClick = { onFilterValueChipClick(chipData.id) }
                        )
                    }
                    items(
                        items = unselectedTopFilterValueChips,
                        key = { chipData -> chipData.id }
                    ) { chipData ->
                        FilterChip(
                            text = chipData.label,
                            selected = false,
                            enabled = enabled,
                            onClick = {
                                pendingScrollChipId = chipData.id
                                onFilterValueChipClick(chipData.id)
                            }
                        )
                    }
                    if (sortSelected || selectedFilterValueChips.isNotEmpty()) {
                        item(
                            key = RESET_CHIP_KEY
                        ) {
                            ResetChip(
                                enabled = enabled,
                                onClick = {
                                    onReset()
                                    scope.launch { topRowState.animateScrollToItem(0) }
                                }
                            )
                        }
                    }
                }
            }
        }

        when {
            filterRibbonData.isEmpty -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(
                        items = listOf(120.dp, 92.dp, 108.dp, 120.dp)
                    ) { width ->
                        Box(
                            modifier = Modifier
                                .width(width)
                                .height(40.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surface4,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                    }
                }
            }
            bottomFilterChips.isNotEmpty() -> {
                LazyRow(
                    state = bottomRowState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = enabled
                ) {
                    bottomFilterChips.forEach { chipData ->
                        item(
                            key = chipData.id
                        ) {
                            TextChip(
                                text = chipData.label,
                                selected = selectedFilterValueChipIds.any { chipId -> chipId.startsWith("${chipData.id}_") },
                                enabled = enabled,
                                onClick = { onFilterChipClick(chipData.id) }
                            )
                        }
                        selectedFilterValueChipsByParentChipId[chipData.id].orEmpty().forEach { selectedChip ->
                            item(
                                key = selectedChip.id
                            ) {
                                FilterChip(
                                    text = selectedChip.label,
                                    selected = true,
                                    enabled = enabled,
                                    onClick = { onFilterValueChipClick(selectedChip.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun FilterChip.parentFilterChipId(filterChipIds: List<String>): String? {
    return filterChipIds.firstOrNull { chipId -> id.startsWith("${chipId}_") }
}

@FontScalePreviews
@Composable
private fun FiltersRowPreview(
    @PreviewParameter(FilterRibbonDataProvider::class) filterRibbonData: FilterRibbonData
) {
    ClientTheme {
        FiltersRow(
            filterRibbonData = filterRibbonData,
            sortSelected = false,
            selectedFilterValueChips = emptyList(),
            enabled = false,
            onSortClick = {},
            onFilterChipClick = {},
            onFilterValueChipClick = {},
            onReset = {}
        )
    }
}
