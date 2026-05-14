package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.coroutines.ClientDispatchers
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

class CartInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val cartRepository: CartRepository
): CartInteractor {

    override val cartProductsFlow: Flow<List<CartProduct>> = cartRepository.cartProductsFlow

    override val cartSize: Flow<Int> = cartRepository.cartSize

    override suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean) {
        withContext(dispatchers.io) { cartRepository.changePaySwitch(product, paySwitch) }
    }

    override suspend fun setProductSize(product: CartProduct, sizeId: String) {
        withContext(dispatchers.io) { cartRepository.setProductSize(product, sizeId) }
    }

    override suspend fun loadBasket() {
        withContext(dispatchers.io) { cartRepository.loadBasket() }
    }

    override suspend fun cartBadge(): Int {
        return withContext(dispatchers.io) { cartRepository.cartBadge() }
    }
}
