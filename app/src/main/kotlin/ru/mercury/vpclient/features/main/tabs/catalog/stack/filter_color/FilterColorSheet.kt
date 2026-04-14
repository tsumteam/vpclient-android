@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.ktx.colorValues
import ru.mercury.vpclient.shared.ktx.quantityWithThousandsSeparator
import ru.mercury.vpclient.shared.ktx.requireQuantity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.filters.FilterColorBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientDragHandle
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.FilterColorSheetStateProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium16
import ru.mercury.vpclient.shared.ui.theme.surface4
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.intent.FilterColorIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.model.FilterColorSheetState

@Composable
fun FilterColorSheet(
    state: FilterColorSheetState,
    dispatch: (FilterColorIntent) -> Unit
) {
    FilterColorSheetContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun FilterColorSheetContent(
    state: FilterColorSheetState,
    dispatch: (FilterColorIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { dispatch(FilterColorIntent.HideFilterColorDialog) },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { ClientDragHandle() }
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
                            dispatch(FilterColorIntent.HideFilterColorDialog)
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
                    text = state.entity.title.uppercase(),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp),
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
                        onClick = { dispatch(FilterColorIntent.ResetFilterColorValues) },
                        contentPadding = PaddingValues(horizontal = 8.dp)
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

            when {
                state.isLoading -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 0.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        userScrollEnabled = false
                    ) {
                        items(9) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(67.dp)
                                        .height(67.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surface4,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .width(67.dp)
                                        .height(12.dp)
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

                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 8.dp)
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F, fill = false)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 0.dp,
                                end = 16.dp,
                                bottom = 88.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = state.entity.colorValues,
                                key = { item -> item.id }
                            ) { item ->
                                FilterColorBox(
                                    text = item.label,
                                    imageUrl = item.imageUrl,
                                    selected = item.id in state.selectedIds,
                                    onClick = { dispatch(FilterColorIntent.ToggleFilterColorValue(item.id)) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        ClientButton(
                            onClick = {
                                scope.launch {
                                    sheetState.hide()
                                    dispatch(FilterColorIntent.ConfirmFilterColorValues)
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

@FontScalePreviews
@Composable
private fun FilterColorSheetPreview(
    @PreviewParameter(FilterColorSheetStateProvider::class) state: FilterColorSheetState
) {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            FilterColorSheet(
                state = state,
                dispatch = {}
            )
        }
    }
}
