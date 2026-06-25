package ru.mercury.vpclient.features.profile_view_history

import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.features.profile_view_history.intent.ProfileViewHistoryIntent
import ru.mercury.vpclient.features.profile_view_history.model.ProfileViewHistoryModel
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileViewHistoryViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    productInteractor: ProductInteractor
): ClientViewModel<ProfileViewHistoryIntent, ProfileViewHistoryModel, Event>(ProfileViewHistoryModel()) {

    val productsPagingFlow = productInteractor.viewHistoryProductsPagingData().cachedIn(this)

    init {
        dispatch(ProfileViewHistoryIntent.CollectCartCount)
        dispatch(ProfileViewHistoryIntent.CollectCartProducts)
        dispatch(ProfileViewHistoryIntent.LoadCartData)
    }

    override fun dispatch(intent: ProfileViewHistoryIntent) {
        when (intent) {
            is ProfileViewHistoryIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(cartCount = count) }
                        }
                }
            }
            is ProfileViewHistoryIntent.CollectCartProducts -> {
                launch {
                    cartInteractor.cartProductsFlow.collectLatest { products ->
                        reduce {
                            it.copy(
                                basketProductIds = products.flatMap { product -> listOf(product.id, product.detailId) }
                                    .filter(String::isNotEmpty)
                                    .toSet(),
                                basketProductKeys = products
                                    .filter { product -> product.itemId.isNotEmpty() && product.colorId.isNotEmpty() }
                                    .map { product -> "${product.itemId}:${product.colorId}:${product.sizeId}" }
                                    .toSet()
                            )
                        }
                    }
                }
            }
            is ProfileViewHistoryIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileViewHistoryIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
            is ProfileViewHistoryIntent.SearchClick -> return
            is ProfileViewHistoryIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileViewHistoryIntent.ProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true)) }
            }
            is ProfileViewHistoryIntent.ProductBasketClick -> launch {
                reduce {
                    it.copy(
                        basketProductIds = it.basketProductIds + intent.product.id,
                        basketProductKeys = it.basketProductKeys + "${intent.product.itemId}:${intent.product.colorId}:"
                    )
                }
                cartInteractor.addProductToBasket(intent.product, null)
                dispatch(ProfileViewHistoryIntent.LoadCartData)
            }
        }
    }
}
