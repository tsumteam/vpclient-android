package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
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

    override suspend fun addProductToBasket(productId: String, sizeId: String?) {
        withContext(dispatchers.io) { cartRepository.addProductToBasket(productId, sizeId) }
    }

    override suspend fun setProductSize(product: CartProduct, sizeId: String) {
        withContext(dispatchers.io) { cartRepository.setProductSize(product, sizeId) }
    }

    override suspend fun deleteProduct(product: CartProduct) {
        withContext(dispatchers.io) { cartRepository.deleteProduct(product) }
    }

    override suspend fun deleteLook(lookId: String) {
        withContext(dispatchers.io) { cartRepository.deleteLook(lookId) }
    }

    override suspend fun disassembleLook(products: List<CartProduct>) {
        withContext(dispatchers.io) { cartRepository.disassembleLook(products) }
    }

    override suspend fun moveProductsAfterDrag(products: List<CartProduct>) {
        withContext(dispatchers.io) { cartRepository.moveProductsAfterDrag(products) }
    }

    override suspend fun removeAlternative(alternative: CartProductAlternative) {
        withContext(dispatchers.io) { cartRepository.removeAlternative(alternative) }
    }

    override suspend fun switchProductWithAlternative(alternative: CartProductAlternative) {
        withContext(dispatchers.io) { cartRepository.switchProductWithAlternative(alternative) }
    }

    override suspend fun basketHideAlternatives(product: CartProduct) {
        withContext(dispatchers.io) { cartRepository.basketHideAlternatives(product) }
    }

    override suspend fun basketShowAlternatives(product: CartProduct) {
        withContext(dispatchers.io) { cartRepository.basketShowAlternatives(product) }
    }

    override suspend fun basketReturnOriginal(product: CartProduct) {
        withContext(dispatchers.io) { cartRepository.basketReturnOriginal(product) }
    }

    override suspend fun loadBasket() {
        withContext(dispatchers.io) { cartRepository.loadBasket() }
    }

    override suspend fun cartBadge(): Int {
        return withContext(dispatchers.io) { cartRepository.cartBadge() }
    }
}
