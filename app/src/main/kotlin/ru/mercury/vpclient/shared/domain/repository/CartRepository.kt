package ru.mercury.vpclient.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CartProduct

interface CartRepository {

    val cartProductsFlow: Flow<List<CartProduct>>

    suspend fun loadCartProducts()

    suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun setProductSize(product: CartProduct, sizeId: String)

    suspend fun cartItemsCount(): Int

    suspend fun cartBadge(): Int
}
