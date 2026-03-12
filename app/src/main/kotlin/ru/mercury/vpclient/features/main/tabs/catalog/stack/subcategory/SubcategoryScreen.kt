package ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.core.ui.components.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.ClientOutlinedButton
import ru.mercury.vpclient.core.ui.components.ClothingRow
import ru.mercury.vpclient.core.ui.components.IndicatorIconButton
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.icons.ChevronStart24
import ru.mercury.vpclient.core.ui.icons.FittingShirt24
import ru.mercury.vpclient.core.ui.icons.Search24
import ru.mercury.vpclient.core.ui.preview.SubcategoryModelProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium17
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.intent.SubcategoryIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.model.SubcategoryModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.navigation.SubcategoryRoute

@Composable
fun SubcategoryScreen(
    route: SubcategoryRoute,
    viewModel: SubcategoryViewModel = hiltViewModel<SubcategoryViewModel, SubcategoryViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    SubcategoryScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun SubcategoryScreenContent(
    state: SubcategoryModel,
    dispatch: (SubcategoryIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.entity.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.medium17.copy(textAlign = TextAlign.Center).onBackground()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.White),
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { dispatch(SubcategoryIntent.BackClick) }
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
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
        bottomBar = {
            ClientOutlinedButton(
                onClick = {},
                text = stringResource(ClientStrings.CatalogViewAllClothing),
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
            )
        }
    ) { innerPadding ->
        when {
            state.pojos.isEmpty() -> {
                ClientLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 8.dp
                    ),
                    userScrollEnabled = false
                ) {
                    items(
                        count = 4,
                        key = { index -> "subcategory_placeholder_$index" }
                    ) { index ->
                        ClothingRow(
                            pojo = SubcategoryPojo(
                                entity = CatalogCategoryEntity.Empty,
                                children = emptyList()
                            )
                        )

                        if (index != 3) {
                            Spacer(
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                }
            }
            else -> {
                ClientLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 8.dp
                    )
                ) {
                    itemsIndexed(
                        items = state.pojos,
                        key = { index, item -> "${item.entity.id}-$index" }
                    ) { index, item ->
                        ClothingRow(
                            pojo = item
                        )

                        if (index != state.pojos.lastIndex) {
                            Spacer(
                                modifier = Modifier.height(24.dp)
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
private fun SubcategoryScreenPreview(
    @PreviewParameter(SubcategoryModelProvider::class) state: SubcategoryModel
) {
    ClientTheme {
        SubcategoryScreenContent(
            state = state,
            dispatch = {}
        )
    }
}
