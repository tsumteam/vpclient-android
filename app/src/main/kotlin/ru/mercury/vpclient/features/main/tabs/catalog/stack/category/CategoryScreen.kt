package ru.mercury.vpclient.features.main.tabs.catalog.stack.category

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogCategorySection
import ru.mercury.vpclient.shared.ui.components.IndicatorIconButton
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientOutlinedButton
import ru.mercury.vpclient.shared.ui.icons.Basket24
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.FittingShirt24
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.preview.CategoryModelProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.medium17
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.intent.CategoryIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.model.CategoryModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.navigation.CategoryRoute

@Composable
fun CategoryScreen(
    route: CategoryRoute,
    viewModel: CategoryViewModel = hiltViewModel<CategoryViewModel, CategoryViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    CategoryScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun CategoryScreenContent(
    state: CategoryModel,
    dispatch: (CategoryIntent) -> Unit
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
                        style = MaterialTheme.typography.medium17.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.White),
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { dispatch(CategoryIntent.BackClick) }
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
                        key = { index -> "category_placeholder_$index" }
                    ) { index ->
                        CatalogCategorySection(
                            pojo = SubcategoryPojo(
                                entity = CatalogCategoryEntity.Empty,
                                children = emptyList()
                            ),
                            onClick = {},
                            onItemClick = {}
                        )

                        if (index != 4) {
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
                        bottom = 16.dp
                    )
                ) {
                    itemsIndexed(
                        items = state.pojos,
                        key = { index, item -> "${item.entity.id}-$index" }
                    ) { index, item ->
                        CatalogCategorySection(
                            pojo = item,
                            onClick = {
                                dispatch(
                                    CategoryIntent.FilterClick(
                                        categoryId = item.entity.id,
                                        titleCategoryId = state.entity.id,
                                        subtitleCategoryId = item.entity.id
                                    )
                                )
                            },
                            onItemClick = { entity ->
                                dispatch(
                                    CategoryIntent.FilterClick(
                                        categoryId = entity.id,
                                        titleCategoryId = entity.id,
                                        subtitleCategoryId = item.entity.id
                                    )
                                )
                            }
                        )

                        if (index != state.pojos.lastIndex) {
                            Spacer(
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }

                    item {
                        ClientOutlinedButton(
                            onClick = {
                                dispatch(
                                    CategoryIntent.FilterClick(
                                        categoryId = state.entity.id,
                                        titleCategoryId = state.entity.rootId,
                                        subtitleCategoryId = state.entity.id
                                    )
                                )
                            },
                            text = stringResource(ClientStrings.CatalogViewAllClothing),
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun CategoryScreenPreview(
    @PreviewParameter(CategoryModelProvider::class) state: CategoryModel
) {
    ClientTheme {
        CategoryScreenContent(
            state = state,
            dispatch = {}
        )
    }
}
