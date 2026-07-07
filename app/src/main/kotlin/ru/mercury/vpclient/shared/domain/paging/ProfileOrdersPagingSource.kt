package ru.mercury.vpclient.shared.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.mercury.vpclient.shared.domain.usecase.ProfileOrdersUseCase
import ru.mercury.vpclient.shared.domain.usecase.UserIdUseCase
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderItemState
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderProductState
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderStatusType
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

class ProfileOrdersPagingSource(
    private val userIdUseCase: UserIdUseCase,
    private val profileOrdersUseCase: ProfileOrdersUseCase,
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
            val clientId = userIdUseCase(Unit).getOrThrow()

            if (clientId.isEmpty()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val orders = profileOrdersUseCase(
                ProfileOrdersUseCase.Params(
                    clientId = clientId,
                    limit = limit,
                    offset = offset
                )
            ).getOrThrow().map { order ->
                val visibleImages = when {
                    order.imageUrls.size <= 4 -> order.imageUrls
                    else -> order.imageUrls.take(4)
                }

                ProfileOrderItemState(
                    numberTitleRes = when {
                        order.isReceipt -> ClientStrings.ProfileOrdersReceiptNumber
                        else -> ClientStrings.ProfileOrdersNumber
                    },
                    orderNumber = order.orderNumber,
                    amount = order.amount,
                    statusPrefix = order.statusPrefix,
                    statusDescription = order.statusDescription,
                    statusType = when {
                        order.isFinished -> ProfileOrderStatusType.Finished
                        else -> ProfileOrderStatusType.NotFinished
                    },
                    showPaymentBadge = order.showPaymentBadge,
                    products = visibleImages.map { imageUrl ->
                        ProfileOrderProductState(
                            imageUrl = imageUrl
                        )
                    },
                    hiddenProductsCount = (order.productsCount - visibleImages.size).coerceAtLeast(0)
                )
            }

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
