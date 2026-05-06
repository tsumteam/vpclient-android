package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.coroutines.ClientDispatchers
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

class CartInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val cartRepository: CartRepository
): CartInteractor {

    override suspend fun cartItemsCount(): Int {
        return withContext(dispatchers.io) { cartRepository.cartItemsCount() }
    }

    override suspend fun cartBadge(): Int {
        return withContext(dispatchers.io) { cartRepository.cartBadge() }
    }
}
