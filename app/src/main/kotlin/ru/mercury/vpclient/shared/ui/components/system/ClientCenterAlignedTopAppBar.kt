@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.system

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.filters.FilterScreenTitle
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

private const val FILTER_NAVIGATION_ICONS_COUNT = 2
private const val TOP_BAR_ICON_SIZE_DP = 42

sealed interface TopBarState {
    data object Logo: TopBarState
    data class Back(
        val navigationClick: () -> Unit
    ): TopBarState
    data class Home(
        val actionsState: TopBarActionsState,
        val searchClick: () -> Unit
    ): TopBarState
    data class Title(
        val title: String,
        val showSearch: Boolean = false,
        val searchClick: () -> Unit = {},
        val actionsState: TopBarActionsState = TopBarActionsState()
    ): TopBarState
    data class Catalog(
        val navigationClick: () -> Unit,
        val actionsState: TopBarActionsState
    ): TopBarState
    data class Details(
        val navigationClick: () -> Unit,
        val onClick: () -> Unit,
        val entity: BrandEntity,
        val showBrandBox: Boolean,
        val actionsState: TopBarActionsState
    ): TopBarState
    data class Category(
        val title: String,
        val navigationClick: () -> Unit,
        val searchClick: () -> Unit,
        val actionsState: TopBarActionsState = TopBarActionsState()
    ): TopBarState
    data class Filter(
        val entity: FilterTitleEntity,
        val onClick: () -> Unit,
        val navigationClick: () -> Unit,
        val searchClick: () -> Unit,
        val actionsState: TopBarActionsState = TopBarActionsState()
    ): TopBarState
    data class FilterBrand(
        val entity: BrandEntity,
        val navigationClick: () -> Unit,
        val searchClick: () -> Unit,
        val actionsState: TopBarActionsState = TopBarActionsState()
    ): TopBarState
}

data class TopBarActionsState(
    val showCartButton: Boolean = false,
    val cartText: String = "",
    val showCartBadge: Boolean = false,
    val cartClick: () -> Unit = {},
    val fittingText: String = "",
    val showFittingButton: Boolean = false,
    val showFittingBadge: Boolean = false,
    val fittingClick: () -> Unit = {},
    val showMessengerButton: Boolean = false,
    val showMessengerBadge: Boolean = false,
    val messengerClick: () -> Unit = {}
)

private val TopBarActionsState.isVisible: Boolean
    get() = showFittingButton || showCartButton || showMessengerButton

private val TopBarActionsState.visibleIconsCount: Int
    get() {
        var count = 0
        if (showFittingButton) count += 1
        if (showCartButton) count += 1
        if (showMessengerButton) count += 1
        return count
    }

@Composable
fun ClientCenterAlignedTopAppBar(
    state: TopBarState
) {
    CenterAlignedTopAppBar(
        title = {
            when (state) {
                is TopBarState.Logo -> {
                    Icon(
                        imageVector = Logo82,
                        contentDescription = null,
                        modifier = Modifier.size(82.dp, 57.dp),
                        tint = Color.Black
                    )
                }
                is TopBarState.Back -> {}
                is TopBarState.Home -> {
                    Icon(
                        imageVector = Logo82,
                        contentDescription = null,
                        modifier = Modifier.size(82.dp, 57.dp),
                        tint = Color.Black
                    )
                }
                is TopBarState.Title -> {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                is TopBarState.Catalog -> {
                    Text(
                        text = stringResource(ClientStrings.MainTabCatalog),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                is TopBarState.Category -> {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                is TopBarState.Filter -> {
                    val navigationIconsWidth = FILTER_NAVIGATION_ICONS_COUNT * TOP_BAR_ICON_SIZE_DP
                    val actionsIconsWidth = state.actionsState.visibleIconsCount * TOP_BAR_ICON_SIZE_DP

                    FilterScreenTitle(
                        entity = state.entity,
                        onClick = state.onClick,
                        modifier = Modifier.padding(
                            start = (actionsIconsWidth - navigationIconsWidth).coerceAtLeast(0).dp,
                            end = (navigationIconsWidth - actionsIconsWidth).coerceAtLeast(0).dp
                        )
                    )
                }
                is TopBarState.FilterBrand -> {
                    BrandBox(
                        entity = state.entity
                    )
                }
                is TopBarState.Details -> {
                    SharedAnimatedVisibility(
                        visible = state.showBrandBox
                    ) {
                        BrandBox(
                            entity = state.entity
                        )
                    }
                }
            }
        },
        modifier = when (state) {
            is TopBarState.Details -> Modifier.clickableWithoutRipple(onClick = state.onClick)
            else -> Modifier
        },
        navigationIcon = {
            when (state) {
                is TopBarState.Home -> {
                    SearchNavigationIcon(onClick = state.searchClick)
                }
                is TopBarState.Title -> {
                    if (state.showSearch) {
                        SearchNavigationIcon(onClick = state.searchClick)
                    }
                }
                is TopBarState.Catalog -> {
                    SearchNavigationIcon(onClick = state.navigationClick)
                }
                is TopBarState.Back -> {
                    IconButton(
                        onClick = state.navigationClick,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                is TopBarState.Details -> {
                    IconButton(
                        onClick = state.navigationClick,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                is TopBarState.Category -> {
                    Row {
                        IconButton(
                            onClick = state.navigationClick,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = state.searchClick,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                is TopBarState.Filter -> {
                    Row {
                        IconButton(
                            onClick = state.navigationClick,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = state.searchClick,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                is TopBarState.FilterBrand -> {
                    Row {
                        IconButton(
                            onClick = state.navigationClick,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = state.searchClick,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                else -> {}
            }
        },
        actions = {
            when (state) {
                is TopBarState.Home -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                is TopBarState.Title -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                is TopBarState.Catalog -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                is TopBarState.Details -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                is TopBarState.Category -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                is TopBarState.Filter -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                is TopBarState.FilterBrand -> {
                    TopBarActions(actionsState = state.actionsState)
                }
                else -> {}
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = when (state) {
                is TopBarState.Category,
                is TopBarState.Filter,
                is TopBarState.FilterBrand,
                is TopBarState.Back,
                is TopBarState.Details -> Color.White
                else -> Color.Transparent
            }
        )
    )
}

@Composable
private fun SearchNavigationIcon(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(42.dp)
    ) {
        Icon(
            imageVector = Search24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun TopBarActions(
    actionsState: TopBarActionsState
) {
    if (!actionsState.isVisible) return

    if (actionsState.showFittingButton) {
        FittingIconButton(
            text = actionsState.fittingText,
            showBadge = actionsState.showFittingBadge,
            onClick = actionsState.fittingClick
        )
    }

    if (actionsState.showCartButton) {
        CartIconButton(
            text = actionsState.cartText,
            showBadge = actionsState.showCartBadge,
            onClick = actionsState.cartClick
        )
    }

    if (actionsState.showMessengerButton) {
        MessengerIconButton(
            showBadge = actionsState.showMessengerBadge,
            onClick = actionsState.messengerClick
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun ClientCenterAlignedTopAppBarPreview(
    @PreviewParameter(TopBarStateProvider::class) state: TopBarState
) {
    ClientCenterAlignedTopAppBar(state = state)
}

private class TopBarStateProvider: PreviewParameterProvider<TopBarState> {
    override val values: Sequence<TopBarState> = sequenceOf(
        TopBarState.Logo,
        TopBarState.Back(
            navigationClick = {}
        ),
        TopBarState.Home(
            actionsState = TopBarActionsState(
                showCartButton = true,
                cartText = "1",
                showCartBadge = true,
                cartClick = {},
                fittingText = "2",
                showFittingButton = true,
                showFittingBadge = true,
                fittingClick = {},
                showMessengerButton = true,
                showMessengerBadge = true,
                messengerClick = {}
            ),
            searchClick = {}
        ),
        TopBarState.Title(title = "Бренды"),
        TopBarState.Catalog(
            navigationClick = {},
            actionsState = TopBarActionsState(
                showCartButton = true,
                cartText = "1",
                showCartBadge = true,
                cartClick = {},
                fittingText = "2",
                showFittingButton = true,
                showFittingBadge = true,
                fittingClick = {},
                showMessengerButton = true,
                showMessengerBadge = true,
                messengerClick = {}
            )
        ),
        TopBarState.Details(
            navigationClick = {},
            onClick = {},
            entity = BrandEntity.Empty,
            showBrandBox = false,
            actionsState = TopBarActionsState()
        ),
        TopBarState.Details(
            navigationClick = {},
            onClick = {},
            entity = BrandEntity(brand = "SAINT LAURENT", urlBrandLogo = null),
            showBrandBox = true,
            actionsState = TopBarActionsState(
                showCartButton = true,
                cartText = "1",
                showCartBadge = true,
                cartClick = {},
                fittingText = "2",
                showFittingButton = true,
                showFittingBadge = true,
                fittingClick = {},
                showMessengerButton = true,
                showMessengerBadge = true,
                messengerClick = {}
            )
        ),
        TopBarState.Category(
            title = "Пуховики",
            navigationClick = {},
            searchClick = {},
            actionsState = TopBarActionsState(
                showCartButton = true,
                cartText = "1",
                showCartBadge = true,
                cartClick = {},
                fittingText = "2",
                showFittingButton = true,
                showFittingBadge = true,
                fittingClick = {},
                showMessengerButton = true,
                showMessengerBadge = true,
                messengerClick = {}
            )
        ),
        TopBarState.Filter(
            entity = FilterTitleEntity.Empty,
            onClick = {},
            navigationClick = {},
            searchClick = {},
            actionsState = TopBarActionsState(
                showCartButton = true,
                cartText = "1",
                showCartBadge = true,
                cartClick = {},
                fittingText = "2",
                showFittingButton = true,
                showFittingBadge = true,
                fittingClick = {},
                showMessengerButton = true,
                showMessengerBadge = true,
                messengerClick = {}
            )
        ),
        TopBarState.FilterBrand(
            entity = BrandEntity(brand = "SAINT LAURENT", urlBrandLogo = null),
            navigationClick = {},
            searchClick = {},
            actionsState = TopBarActionsState(
                showCartButton = true,
                cartText = "1",
                showCartBadge = true,
                cartClick = {},
                fittingText = "2",
                showFittingButton = true,
                showFittingBadge = true,
                fittingClick = {},
                showMessengerButton = true,
                showMessengerBadge = true,
                messengerClick = {}
            )
        )
    )
}
