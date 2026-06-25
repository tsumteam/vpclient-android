package ru.mercury.vpclient.shared.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationResult
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity

interface CartInteractor {

    val cartProductsFlow: Flow<List<CartProduct>>

    suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun changeFittingPaySwitch(product: CartProduct, paySwitch: Boolean)

    suspend fun addProductToBasket(productId: String, sizeId: String?)

    suspend fun addProductToBasket(product: CatalogFilterProductsEntity, sizeId: String?)

    suspend fun setProductSize(product: CartProduct, sizeId: String)

    suspend fun addProductSize(product: CartProduct, sizeId: String)

    suspend fun removeProductSize(product: CartProduct, size: CartProductSize)

    suspend fun setProductColor(product: CartProduct, colorId: String)

    suspend fun setProductQuantity(product: CartProduct, quantity: Int)

    suspend fun fittingReturnProduct(product: CartProduct)

    suspend fun setFittingProductSize(product: CartProduct, sizeId: String)

    suspend fun setFittingProductColor(product: CartProduct, colorId: String)

    suspend fun loadAvailableSizes(product: CartProduct): ProductAvailableSizesEntity

    suspend fun loadAvailableColors(product: CartProduct): List<ProductAvailableColor>

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
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?
    ): FittingConfirmationData

    suspend fun loadExistingFittingConfirmationData(
        products: List<CartProduct>,
        deliveryId: String,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?
    ): FittingConfirmationData

    suspend fun confirmFitting(
        products: List<CartProduct>,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?,
        singleInterval: FittingConfirmationDeliveryInterval?,
        deliveryGroups: List<FittingConfirmationDeliveryGroup>,
        selectedDeliveryIntervalIds: Map<String, String>,
        useSingleDelivery: Boolean
    ): FittingConfirmationResult

    suspend fun loadClientDeliveryAddresses(): List<ClientDeliveryAddress>

    suspend fun searchClientDeliveryAddress(query: String): List<ClientDeliveryAddressSuggestion>

    suspend fun createClientDeliveryAddress(address: ClientDeliveryAddress): ClientDeliveryAddress

    suspend fun updateClientDeliveryAddress(address: ClientDeliveryAddress): ClientDeliveryAddress

    suspend fun deleteClientDeliveryAddress(addressId: Int)

    suspend fun cartBadge(): Int
}
