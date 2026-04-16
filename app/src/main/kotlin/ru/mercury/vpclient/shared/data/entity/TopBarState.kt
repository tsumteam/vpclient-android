package ru.mercury.vpclient.shared.data.entity

sealed interface TopBarState {
    data object Logo: TopBarState
    data class Title(
        val title: String
    ): TopBarState
    data class Catalog(
        val navigationClick: () -> Unit
    ): TopBarState
    data class Details(
        val navigationClick: () -> Unit
    ): TopBarState
    data class Category(
        val title: String,
        val navigationClick: () -> Unit,
        val searchClick: () -> Unit
    ): TopBarState
    data class Filter(
        val entity: FilterTitleEntity,
        val onClick: () -> Unit,
        val navigationClick: () -> Unit,
        val searchClick: () -> Unit
    ): TopBarState
}
