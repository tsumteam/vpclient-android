package ru.mercury.vpclient.shared.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative

interface CartInteractor {

    val cartProductsFlow: Flow<List<CartProduct>>

    val cartSize: Flow<Int>

    suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun addProductToBasket(productId: String, sizeId: String?)

    suspend fun setProductSize(product: CartProduct, sizeId: String)

    suspend fun deleteProduct(product: CartProduct)

    suspend fun deleteLook(lookId: String)

    suspend fun disassembleLook(products: List<CartProduct>)

    suspend fun removeAlternative(alternative: CartProductAlternative)

    suspend fun switchProductWithAlternative(alternative: CartProductAlternative)

    suspend fun basketHideAlternatives(product: CartProduct)

    suspend fun basketShowAlternatives(product: CartProduct)

    suspend fun basketReturnOriginal(product: CartProduct)

    suspend fun loadBasket()

    suspend fun cartBadge(): Int
}
