@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.entity.SizeCountry
import ru.mercury.vpclient.core.ktx.displayLabel
import ru.mercury.vpclient.core.ktx.quantityWithThousandsSeparator
import ru.mercury.vpclient.core.ktx.requireQuantity
import ru.mercury.vpclient.core.ktx.sizeValues
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.components.ClientAnimatedVisibility
import ru.mercury.vpclient.core.ui.components.ClientButton
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.DragHandle
import ru.mercury.vpclient.core.ui.components.ListRow2
import ru.mercury.vpclient.core.ui.components.SizeFrenchButton
import ru.mercury.vpclient.core.ui.components.SizeInternationalButton
import ru.mercury.vpclient.core.ui.components.SizeItalianButton
import ru.mercury.vpclient.core.ui.components.SizeRussianButton
import ru.mercury.vpclient.core.ui.icons.Close24
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.FilterSizeSheetStateProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.shimmer
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium19
import ru.mercury.vpclient.core.ui.theme.medium16
import ru.mercury.vpclient.core.ui.theme.regular15
import ru.mercury.vpclient.core.ui.theme.secondary6
import ru.mercury.vpclient.core.ui.theme.surface4
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.intent.FilterSizeIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.model.FilterSizeSheetState

@Composable
fun FilterSizeSheet(
    state: FilterSizeSheetState,
    dispatch: (FilterSizeIntent) -> Unit
) {
    FilterSizeSheetContent(
        state = state,
        dispatch = dispatch
    )
}

// fixme

@Composable
private fun FilterSizeSheetContent(
    state: FilterSizeSheetState,
    dispatch: (FilterSizeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var selectedCountry by remember { mutableStateOf(SizeCountry.Russia) }

    ModalBottomSheet(
        onDismissRequest = { dispatch(FilterSizeIntent.HideFilterSizeDialog) },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { DragHandle() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            dispatch(FilterSizeIntent.HideFilterSizeDialog)
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
                    text = stringResource(ClientStrings.FilterSizeTitle),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp, vertical = 0.dp),
                    style = MaterialTheme.typography.livretMedium19.copy(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
                )

                ClientAnimatedVisibility(
                    visible = state.selectedIds.isNotEmpty(),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(
                        onClick = { dispatch(FilterSizeIntent.ResetFilterSizeValues) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CommonReset),
                            style = MaterialTheme.typography.medium16.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }

            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surface4,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 16.dp)
                                .width(120.dp)
                                .height(19.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surface4,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )

                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 4.dp)
                                    .height(48.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surface4,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = MaterialTheme.colorScheme.surface4,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
                else -> {
                    val visibleSizeValues = state.entity.sizeValues.filter { item ->
                        item.displayLabel(selectedCountry) != null
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F, fill = false)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                            ) {
                                SizeCountry.entries.forEach { country ->
                                    when (country) {
                                        SizeCountry.Russia -> {
                                            SizeRussianButton(
                                                selected = selectedCountry == country,
                                                onClick = { selectedCountry = country }
                                            )
                                        }
                                        SizeCountry.Italy -> {
                                            SizeItalianButton(
                                                selected = selectedCountry == country,
                                                onClick = { selectedCountry = country }
                                            )
                                        }
                                        SizeCountry.France -> {
                                            SizeFrenchButton(
                                                selected = selectedCountry == country,
                                                onClick = { selectedCountry = country }
                                            )
                                        }
                                        SizeCountry.International -> {
                                            SizeInternationalButton(
                                                selected = selectedCountry == country,
                                                onClick = { selectedCountry = country }
                                            )
                                        }
                                    }
                                }
                            }

                            Text(
                                text = selectedCountry.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 8.dp),
                                style = MaterialTheme.typography.regular15.copy(
                                    color = MaterialTheme.colorScheme.secondary6,
                                    lineHeight = 19.sp,
                                    letterSpacing = .2.sp,
                                    textAlign = TextAlign.Center
                                )
                            )

                            ClientLazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 72.dp)
                            ) {
                                itemsIndexed(
                                    items = visibleSizeValues,
                                    key = { _, item -> item.id }
                                ) { index, item ->
                                    ListRow2(
                                        text = requireNotNull(item.displayLabel(selectedCountry)),
                                        selected = state.selectedIds.contains(item.id),
                                        onClick = { dispatch(FilterSizeIntent.ToggleFilterSizeValue(item.id)) }
                                    )

                                    if (index != visibleSizeValues.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(start = 48.dp),
                                            color = MaterialTheme.colorScheme.surface4
                                        )
                                    }
                                }
                            }
                        }

                        ClientButton(
                            onClick = {
                                scope.launch {
                                    sheetState.hide()
                                    dispatch(FilterSizeIntent.ConfirmFilterSizeValues)
                                }
                            },
                            text = pluralStringResource(ClientStrings.FilterShowProductsQuantity, state.quantityEntity.requireQuantity, state.quantityEntity.quantityWithThousandsSeparator),
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
}

// fixme
private val SizeCountry.title: String
    get() {
        return when (this) {
            SizeCountry.Russia -> "Россия"
            SizeCountry.Italy -> "Италия"
            SizeCountry.France -> "Франция"
            SizeCountry.International -> "Международная"
        }
    }

@FontScalePreviews
@Composable
private fun FilterSizeSheetPreview(
    @PreviewParameter(FilterSizeSheetStateProvider::class) state: FilterSizeSheetState
) {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            FilterSizeSheet(
                state = state,
                dispatch = {}
            )
        }
    }
}
