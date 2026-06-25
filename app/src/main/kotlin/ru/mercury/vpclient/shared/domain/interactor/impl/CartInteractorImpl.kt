package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

class CartInteractorImpl @Inject constructor(
    private val dispatchers: SharedDispatchers,
    private val cartRepository: CartRepository
): CartInteractor {

    override val cartProductsFlow: Flow<List<CartProduct>> = cartRepository.cartProductsFlow

    override suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean) {
        withContext(dispatchers.io) { cartRepository.changePaySwitch(product, paySwitch) }
    }

    override suspend fun changeFittingPaySwitch(product: CartProduct, paySwitch: Boolean) {
        withContext(dispatchers.io) { cartRepository.changeFittingPaySwitch(product, paySwitch) }
    }

    override suspend fun addProductToBasket(productId: String, sizeId: String?) {
        withContext(dispatchers.io) { cartRepository.addProductToBasket(productId, sizeId) }
    }

    override suspend fun addProductToBasket(product: CatalogFilterProductsEntity, sizeId: String?) {
        withContext(dispatchers.io) { cartRepository.addProductToBasket(product, sizeId) }
    }

    override suspend fun setProductSize(product: CartProduct, sizeId: String) {
        withContext(dispatchers.io) { cartRepository.setProductSize(product, sizeId) }
    }

    override suspend fun addProductSize(product: CartProduct, sizeId: String) {
        withContext(dispatchers.io) { cartRepository.addProductSize(product, sizeId) }
    }

    override suspend fun removeProductSize(product: CartProduct, size: CartProductSize) {
        withContext(dispatchers.io) { cartRepository.removeProductSize(product, size) }
    }

    override suspend fun setProductColor(product: CartProduct, colorId: String) {
        withContext(dispatchers.io) { cartRepository.setProductColor(product, colorId) }
    }

    override suspend fun setProductQuantity(product: CartProduct, quantity: Int) {
        withContext(dispatchers.io) { cartRepository.setProductQuantity(product, quantity) }
    }

    override suspend fun fittingReturnProduct(product: CartProduct) {
        withContext(dispatchers.io) { cartRepository.fittingReturnProduct(product) }
    }

    override suspend fun setFittingProductSize(product: CartProduct, sizeId: String) {
        withContext(dispatchers.io) { cartRepository.setFittingProductSize(product, sizeId) }
    }

    override suspend fun setFittingProductColor(product: CartProduct, colorId: String) {
        withContext(dispatchers.io) { cartRepository.setFittingProductColor(product, colorId) }
    }

    override suspend fun loadAvailableSizes(product: CartProduct): ProductAvailableSizesEntity {
        return withContext(dispatchers.io) { cartRepository.loadAvailableSizes(product) }
    }

    override suspend fun loadAvailableColors(product: CartProduct): List<ProductAvailableColor> {
        return withContext(dispatchers.io) { cartRepository.loadAvailableColors(product) }
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

    override suspend fun loadFitting(): FittingData {
        return withContext(dispatchers.io) { cartRepository.loadFitting() }
    }

    override suspend fun loadFittingConfirmationData(
        products: List<CartProduct>,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?
    ): FittingConfirmationData {
        return withContext(dispatchers.io) {
            cartRepository.loadFittingConfirmationData(products, fittingType, clientAddress)
        }
    }

    override suspend fun loadExistingFittingConfirmationData(
        products: List<CartProduct>,
        deliveryId: String,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?
    ): FittingConfirmationData {
        return withContext(dispatchers.io) {
            cartRepository.loadExistingFittingConfirmationData(
                products = products,
                deliveryId = deliveryId,
                fittingType = fittingType,
                clientAddress = clientAddress
            )
        }
    }

    override suspend fun confirmFitting(
        products: List<CartProduct>,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?,
        singleInterval: FittingConfirmationDeliveryInterval?,
        deliveryGroups: List<FittingConfirmationDeliveryGroup>,
        selectedDeliveryIntervalIds: Map<String, String>,
        useSingleDelivery: Boolean
    ): FittingConfirmationResult {
        return withContext(dispatchers.io) {
            cartRepository.confirmFitting(
                products = products,
                fittingType = fittingType,
                clientAddress = clientAddress,
                singleInterval = singleInterval,
                deliveryGroups = deliveryGroups,
                selectedDeliveryIntervalIds = selectedDeliveryIntervalIds,
                useSingleDelivery = useSingleDelivery
            )
        }
    }

    override suspend fun loadClientDeliveryAddresses(): List<ClientDeliveryAddress> {
        return withContext(dispatchers.io) { cartRepository.loadClientDeliveryAddresses() }
    }

    override suspend fun searchClientDeliveryAddress(
        query: String
    ): List<ClientDeliveryAddressSuggestion> {
        return withContext(dispatchers.io) { cartRepository.searchClientDeliveryAddress(query) }
    }

    override suspend fun createClientDeliveryAddress(
        address: ClientDeliveryAddress
    ): ClientDeliveryAddress {
        return withContext(dispatchers.io) { cartRepository.createClientDeliveryAddress(address) }
    }

    override suspend fun updateClientDeliveryAddress(
        address: ClientDeliveryAddress
    ): ClientDeliveryAddress {
        return withContext(dispatchers.io) { cartRepository.updateClientDeliveryAddress(address) }
    }

    override suspend fun deleteClientDeliveryAddress(addressId: Int) {
        withContext(dispatchers.io) { cartRepository.deleteClientDeliveryAddress(addressId) }
    }

    override suspend fun cartBadge(): Int {
        return withContext(dispatchers.io) { cartRepository.cartBadge() }
    }
}
