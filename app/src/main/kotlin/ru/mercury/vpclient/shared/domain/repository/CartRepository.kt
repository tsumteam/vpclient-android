package ru.mercury.vpclient.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationResult
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum

interface CartRepository {

    val cartProductsFlow: Flow<List<CartProduct>>

    val cartSize: Flow<Int>

    suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun changeFittingPaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun addProductToBasket(productId: String, sizeId: String?)

    suspend fun setProductSize(product: CartProduct, sizeId: String)

    suspend fun deleteProduct(product: CartProduct)

    suspend fun deleteLook(lookId: String)

    suspend fun disassembleLook(products: List<CartProduct>)

    suspend fun moveProductsAfterDrag(products: List<CartProduct>)

    suspend fun removeAlternative(alternative: CartProductAlternative)

    suspend fun switchProductWithAlternative(alternative: CartProductAlternative)

    suspend fun basketHideAlternatives(product: CartProduct)

    suspend fun basketShowAlternatives(product: CartProduct)

    suspend fun basketReturnOriginal(product: CartProduct)

    suspend fun loadBasket()

    suspend fun loadFitting(): FittingData

    suspend fun loadFittingConfirmationData(
        products: List<CartProduct>,
        fittingType: FittingTypeDtoEnum
    ): FittingConfirmationData

    suspend fun loadExistingFittingConfirmationData(
        products: List<CartProduct>,
        deliveryId: String,
        fittingType: FittingTypeDtoEnum
    ): FittingConfirmationData

    suspend fun confirmFitting(
        products: List<CartProduct>,
        fittingType: FittingTypeDtoEnum,
        singleInterval: FittingConfirmationDeliveryInterval?,
        deliveryGroups: List<FittingConfirmationDeliveryGroup>,
        selectedDeliveryIntervalIds: Map<String, String>,
        useSingleDelivery: Boolean
    ): FittingConfirmationResult

    suspend fun cartBadge(): Int
}
