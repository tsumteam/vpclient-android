package ru.mercury.vpclient.features.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.category.intent.CategoryIntent
import ru.mercury.vpclient.features.category.model.CategoryModel
import ru.mercury.vpclient.features.category.navigation.CategoryRoute
import ru.mercury.vpclient.shared.data.network.response.CatalogCategoryResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogCategorySection
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.TopBarActionsState
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretRegular15

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
    SharedScaffold(
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Category(
                    title = state.entity.name,
                    navigationClick = { dispatch(CategoryIntent.BackClick) },
                    searchClick = {},
                    actionsState = TopBarActionsState(
                        showCartButton = true,
                        cartText = state.cartText,
                        showCartBadge = state.showCartBadge,
                        cartClick = { dispatch(CategoryIntent.CartClick) },
                        fittingText = state.fittingText,
                        showFittingButton = state.showFittingButton,
                        showFittingBadge = state.showFittingBadge,
                        fittingClick = { dispatch(CategoryIntent.FittingClick) },
                        showMessengerButton = true,
                        showMessengerBadge = state.showMessengerBadge,
                        messengerClick = { dispatch(CategoryIntent.MessengerClick) }
                    )
                )
            )
        }
    ) { innerPadding ->
        when {
            state.pojos.isEmpty() -> {
                SharedLazyColumn(
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
                SharedLazyColumn(
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
                        OutlinedButton(
                            onClick = {
                                dispatch(
                                    CategoryIntent.FilterClick(
                                        categoryId = state.entity.id,
                                        titleCategoryId = state.entity.rootId,
                                        subtitleCategoryId = state.entity.id
                                    )
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(
                                text = stringResource(ClientStrings.CatalogViewAllClothing),
                                style = MaterialTheme.typography.livretRegular15.copy(
                                    textAlign = TextAlign.Center
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
private fun CategoryScreenPreview(
    @PreviewParameter(CategoryModelProvider::class) state: CategoryModel
) {
    CategoryScreenContent(
        state = state,
        dispatch = {}
    )
}

private class CategoryModelProvider: PreviewParameterProvider<CategoryModel> {
    private val catalogCategoryEntity = CatalogCategoryEntity(
        id = 10,
        parentId = 2,
        rootId = 2,
        level = CatalogCategoryEntity.LEVEL_TOP,
        name = "Одежда",
        photoUrl = "",
        categoryType = CatalogCategoryResponse.CATEGORY_TYPE_CATALOG,
        sortSettingId = 0,
        position = 1
    )
    private val childCategoryEntity = CatalogCategoryEntity(
        id = 1,
        parentId = 10,
        rootId = 2,
        level = CatalogCategoryEntity.LEVEL_BOTTOM,
        name = "ПУХОВЫЕ",
        photoUrl = "",
        categoryType = "",
        sortSettingId = 0,
        position = 1
    )
    private val categoryPojo = SubcategoryPojo(
        entity = catalogCategoryEntity.copy(
            level = CatalogCategoryEntity.LEVEL_BOTTOM,
            name = "КУРТКИ"
        ),
        children = listOf(childCategoryEntity)
    )

    override val values: Sequence<CategoryModel> = sequenceOf(
        CategoryModel(
            entity = catalogCategoryEntity,
            pojos = listOf(categoryPojo)
        ),
        CategoryModel()
    )
}
