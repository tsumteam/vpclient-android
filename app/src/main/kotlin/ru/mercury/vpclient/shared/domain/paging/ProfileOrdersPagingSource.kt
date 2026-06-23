package ru.mercury.vpclient.shared.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.mercury.vpclient.features.profile_orders.model.toProfileOrderItemState
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.interactor.OrderInteractor
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderItemState

class ProfileOrdersPagingSource(
    private val authenticationInteractor: AuthenticationInteractor,
    private val orderInteractor: OrderInteractor,
    private val pageSize: Int
): PagingSource<Int, ProfileOrderItemState>() {

    override fun getRefreshKey(state: PagingState<Int, ProfileOrderItemState>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(pageSize) ?: anchorPage.nextKey?.minus(pageSize)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProfileOrderItemState> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize
            val clientId = authenticationInteractor.userId()

            if (clientId.isEmpty()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val orders = orderInteractor.profileOrders(
                clientId = clientId,
                limit = limit,
                offset = offset
            ).map { order -> order.toProfileOrderItemState() }

            LoadResult.Page(
                data = orders,
                prevKey = when {
                    offset == 0 -> null
                    else -> (offset - pageSize).coerceAtLeast(0)
                },
                nextKey = when {
                    orders.size < limit -> null
                    else -> offset + orders.size
                }
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}