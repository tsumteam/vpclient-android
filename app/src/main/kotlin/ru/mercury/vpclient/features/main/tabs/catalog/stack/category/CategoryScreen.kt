package ru.mercury.vpclient.features.main.tabs.catalog.stack.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.intent.CategoryIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.model.CategoryModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.navigation.CategoryRoute
import ru.mercury.vpclient.shared.data.entity.TopBarState
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogCategorySection
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientOutlinedButton
import ru.mercury.vpclient.shared.ui.preview.CategoryModelProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

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
                state = TopBarState.Category(
                    title = state.entity.name,
                    navigationClick = { dispatch(CategoryIntent.BackClick) },
                    searchClick = {}
                )
            )
        }
    ) { innerPadding ->
        when {
            state.pojos.isEmpty() -> {
                ClientLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    userScrollEnabled = false
                ) {
                    items(
                        count = 4,
                        key = { index -> "category_placeholder_$index" }
                    ) {
                        CatalogCategorySection(
                            pojo = SubcategoryPojo(
                                entity = CatalogCategoryEntity.Empty,
                                children = emptyList()
                            ),
                            onClick = {},
                            onItemClick = {}
                        )
                    }
                }
            }
            else -> {
                ClientLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(
                        items = state.pojos,
                        key = { item -> "${item.entity.id}" }
                    ) { item ->
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
                            modifier = Modifier.padding(horizontal = 16.dp)
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
