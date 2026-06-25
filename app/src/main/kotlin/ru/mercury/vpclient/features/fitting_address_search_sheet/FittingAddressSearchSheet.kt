@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_address_search_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.fitting_address_search_sheet.intent.FittingAddressSearchSheetIntent
import ru.mercury.vpclient.features.fitting_address_search_sheet.model.FittingAddressSearchModel
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun FittingAddressSearchSheet(
    state: FittingAddressSearchModel,
    dispatch: (FittingAddressSearchSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedModalBottomSheet(
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding(),
        onDismissRequest = { dispatch(FittingAddressSearchSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        SharedScaffold(
            topBar = {
                Column {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(ClientStrings.FittingAddressNewDeliveryTitle),
                                style = MaterialTheme.typography.livretMedium18
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { dispatch(FittingAddressSearchSheetIntent.DismissRequest) }
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

                    ClientTextField(
                        value = state.query,
                        onValueChange = { value ->
                            dispatch(FittingAddressSearchSheetIntent.QueryChange(value))
                        },
                        label = stringResource(ClientStrings.FittingAddressCityStreetHousePlaceholder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = innerPadding + PaddingValues(vertical = 16.dp)
            ) {
                when {
                    state.isSuggestionsLoading -> {
                        items(3) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(horizontal = 16.dp)
                                    .wrapContentHeight(Alignment.CenterVertically)
                                    .height(44.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )
                        }
                    }
                    else -> {
                        items(
                            items = state.suggestions,
                            key = { suggestion ->
                                "${suggestion.title}_${suggestion.latitude}_${suggestion.longitude}"
                            }
                        ) { suggestion ->
                            Text(
                                text = suggestion.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clickable {
                                        dispatch(FittingAddressSearchSheetIntent.SelectAddressSuggestion(suggestion))
                                    }
                                    .padding(horizontal = 16.dp)
                                    .wrapContentHeight(Alignment.CenterVertically),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.medium15.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    letterSpacing = .3.sp
                                )
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
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
private fun FittingAddressSearchSheetPreview(
    @PreviewParameter(FittingAddressSearchModelProvider::class) state: FittingAddressSearchModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FittingAddressSearchSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class FittingAddressSearchModelProvider: PreviewParameterProvider<FittingAddressSearchModel> {
    override val values: Sequence<FittingAddressSearchModel> = sequenceOf(
        FittingAddressSearchModel(
            query = "Москва, Тверская",
            suggestions = listOf(
                ClientDeliveryAddressSuggestion("г. Москва, ул. Тверская, д. 14"),
                ClientDeliveryAddressSuggestion("г. Москва, ул. Тверская, д. 9")
            )
        ),
        FittingAddressSearchModel(
            query = "Москва, Тверская",
            isSuggestionsLoading = true
        )
    )
}
