package ru.mercury.vpclient.shared.ui.ktx

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

private val LazyPagingItems<*>.mediatedRefreshState: LoadState
    get() = loadState.mediator?.refresh ?: loadState.refresh

private val LazyPagingItems<*>.mediatedAppendState: LoadState
    get() = loadState.mediator?.append ?: loadState.append

val LazyPagingItems<*>.isRefreshLoading: Boolean
    get() = mediatedRefreshState is LoadState.Loading

val LazyPagingItems<*>.isRefreshFailure: Boolean
    get() = mediatedRefreshState is LoadState.Error && itemCount == 0

val LazyPagingItems<*>.isContentVisible: Boolean
    get() = itemCount > 0 || !isRefreshLoading && !isRefreshFailure

val LazyPagingItems<*>.isPagingLoading: Boolean
    get() = itemCount > 0 && mediatedAppendState is LoadState.Loading

val LazyPagingItems<*>.isPagingFailure: Boolean
    get() = itemCount > 0 && mediatedAppendState is LoadState.Error
