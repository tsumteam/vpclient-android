package ru.mercury.vpclient.shared.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CartProduct

interface CartInteractor {

    val cartProductsFlow: Flow<List<CartProduct>>

    val cartSize: Flow<Int>

    suspend fun loadCartProducts()

    suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun setProductSize(product: CartProduct, sizeId: String)

    suspend fun loadBasket()

    suspend fun cartBadge(): Int
}
