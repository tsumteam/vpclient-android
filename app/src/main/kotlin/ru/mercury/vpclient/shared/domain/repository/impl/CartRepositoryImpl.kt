package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationResult
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationResultDeliveryLine
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.data.error.AddProductSizeException
import ru.mercury.vpclient.shared.data.error.AddProductToBasketException
import ru.mercury.vpclient.shared.data.error.BasketHideAlternativesException
import ru.mercury.vpclient.shared.data.error.BasketReturnOriginalException
import ru.mercury.vpclient.shared.data.error.BasketShowAlternativesException
import ru.mercury.vpclient.shared.data.error.ChangeFittingLineColorException
import ru.mercury.vpclient.shared.data.error.ChangeFittingLineSizeException
import ru.mercury.vpclient.shared.data.error.ChangeFittingPaySwitchException
import ru.mercury.vpclient.shared.data.error.ChangePaySwitchException
import ru.mercury.vpclient.shared.data.error.ClientAddressException
import ru.mercury.vpclient.shared.data.error.ConfirmFittingException
import ru.mercury.vpclient.shared.data.error.DeleteLookException
import ru.mercury.vpclient.shared.data.error.DeleteProductException
import ru.mercury.vpclient.shared.data.error.DisassembleLookException
import ru.mercury.vpclient.shared.data.error.MoveProductsAfterDragException
import ru.mercury.vpclient.shared.data.error.RemoveAlternativeException
import ru.mercury.vpclient.shared.data.error.RemoveProductSizeException
import ru.mercury.vpclient.shared.data.error.SetProductColorException
import ru.mercury.vpclient.shared.data.error.SetProductQuantityException
import ru.mercury.vpclient.shared.data.error.SetProductSizeException
import ru.mercury.vpclient.shared.data.error.SwitchProductWithAlternativeException
import ru.mercury.vpclient.shared.data.error.FittingReturnProductException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AvailableColorsRequest
import ru.mercury.vpclient.shared.data.network.request.AvailableSizesRequest
import ru.mercury.vpclient.shared.data.network.request.BasketGetDeliveryTimesForFittingRequest
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.request.CreateClientAddressRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteClientAddressRequest
import ru.mercury.vpclient.shared.data.network.response.FittingChangeLineColorOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.FittingChangeLineSizeOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.FittingDeliveryResponse
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.request.GetDeliveryIntervalsForExistingFittingRequest
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressForCheckoutRequest
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressRequest
import ru.mercury.vpclient.shared.data.network.type.KittingType
import ru.mercury.vpclient.shared.data.network.request.FittingOperationRequest
import ru.mercury.vpclient.shared.data.network.type.FittingOperationRequestType
import ru.mercury.vpclient.shared.data.network.response.FittingReturnProductOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.response.TransferBasketToFittingLineResponse
import ru.mercury.vpclient.shared.data.network.request.TransferBasketToFittingRequest
import ru.mercury.vpclient.shared.data.network.request.UpdateClientAddressRequest
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FittingProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.addProductToBasketRequest
import ru.mercury.vpclient.shared.domain.mapper.addProductSizeRequest
import ru.mercury.vpclient.shared.domain.mapper.basketHideAlternativesRequest
import ru.mercury.vpclient.shared.domain.mapper.basketReturnOriginalRequest
import ru.mercury.vpclient.shared.domain.mapper.basketShowAlternativesRequest
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import ru.mercury.vpclient.shared.domain.mapper.cartProductWithLook
import ru.mercury.vpclient.shared.domain.mapper.cartProductsAfterDragRequest
import ru.mercury.vpclient.shared.domain.mapper.catalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.changeColorRequest
import ru.mercury.vpclient.shared.domain.mapper.changeQuantityRequest
import ru.mercury.vpclient.shared.domain.mapper.changeSizeRequest
import ru.mercury.vpclient.shared.domain.mapper.deleteLookRequest
import ru.mercury.vpclient.shared.domain.mapper.deleteProductRequest
import ru.mercury.vpclient.shared.domain.mapper.disassembleLookRequest
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.fittingEntity
import ru.mercury.vpclient.shared.domain.mapper.fittingPaySwitchRequest
import ru.mercury.vpclient.shared.domain.mapper.fittingDeliveryHeader
import ru.mercury.vpclient.shared.domain.mapper.fittingConfirmationData
import ru.mercury.vpclient.shared.domain.mapper.deliveryTimeDto
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.clientAddressBaseDto
import ru.mercury.vpclient.shared.domain.mapper.clientAddressDto
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddress
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.domain.mapper.paySwitchRequest
import ru.mercury.vpclient.shared.domain.mapper.coordinateDto
import ru.mercury.vpclient.shared.domain.mapper.removeAlternativeRequest
import ru.mercury.vpclient.shared.domain.mapper.removeProductSizeRequest
import ru.mercury.vpclient.shared.domain.mapper.switchProductWithAlternativeRequest
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

private const val ADD_PRODUCT_TO_BASKET_ERROR_MESSAGE = "Не удалось добавить товар в корзину"

private val fittingOperationJson = Json {
    explicitNulls = false
}

class CartRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val cartProductDao: CartProductDao,
    private val fittingProductDao: FittingProductDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    private val settingsDataStore: SettingsDataStore
): CartRepository {

    override val cartProductsFlow: Flow<List<CartProduct>> = cartProductDao.selectAllFlow()
        .map { entities -> entities.map { it.cartProduct } }

    override suspend fun loadBasket() {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            cartProductDao.delete()
            return
        }

        handleResponse(
            request = { networkService.basketByPairedUserId(pairedUserId) },
            onSuccess = { cart ->
                val lines = cart.lines.orEmpty()
                val looks = cart.looks.orEmpty()
                val products = lines.mapNotNull { it.cartProductWithLook(looks) }
                val catalogFilterProductsEntities = products.mapIndexedNotNull { index, product ->
                    product.catalogFilterProductsEntity(index)
                }
                val cartProductEntities = products.mapIndexed { index, product ->
                    product.entity(index)
                }
                catalogFilterProductsDao.upsert(catalogFilterProductsEntities)
                appDatabase.withTransaction {
                    cartProductDao.run {
                        delete()
                        upsert(cartProductEntities)
                    }
                }
            }
        )
    }

    override suspend fun loadFitting(): FittingData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            fittingProductDao.delete()
            return FittingData()
        }

        val fitting = handleResponseResult {
            networkService.fittingsByPairedUserId(pairedUserId)
        }.getOrThrow()
        val deliveries = fitting.deliveries.orEmpty().sortedBy { it.order ?: Int.MAX_VALUE }
        val fittingDeliveries = deliveries.map { delivery ->
            FittingDeliveryData(
                id = delivery.deliveryId.orEmpty(),
                fittingType = delivery.fittingType ?: FittingType.IN_THE_STORE,
                header = delivery.fittingDeliveryHeader,
                products = delivery.lines.orEmpty()
                    .sortedBy { it.order ?: Int.MAX_VALUE }
                    .mapNotNull { it.cartProduct }
            )
        }
        val fittingProductEntities = fittingDeliveries.flatMapIndexed { deliveryIndex, delivery ->
            delivery.products.mapIndexed { productIndex, product ->
                product.fittingEntity(
                    deliveryId = delivery.id,
                    deliveryPosition = deliveryIndex,
                    position = productIndex
                )
            }
        }
        appDatabase.withTransaction {
            fittingProductDao.delete()
            fittingProductDao.upsert(fittingProductEntities)
        }

        return FittingData(
            deliveries = fittingDeliveries
        )
    }

    override suspend fun loadFittingConfirmationData(
        products: List<CartProduct>,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?
    ): FittingConfirmationData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return FittingConfirmationData()
        }

        val addressRequest = GetListClientAddressForCheckoutRequest(
            pairedUserId = pairedUserId
        )
        val addresses = handleResponseResult {
            networkService.clientAddressCheckout(addressRequest)
        }.getOrThrow()
        val address = when (fittingType) {
            FittingType.NONE -> addresses.boutiqueAddress?.address
            FittingType.IN_THE_STORE -> addresses.boutiqueAddress?.address
            FittingType.AT_HOME -> clientAddress?.address ?: addresses.clientAddress?.address
        }
        val addressComment = when (fittingType) {
            FittingType.NONE -> ""
            FittingType.IN_THE_STORE -> ""
            FittingType.AT_HOME -> clientAddress?.comment ?: addresses.clientAddress?.comment.orEmpty()
        }
        val deliveryTimesRequest = BasketGetDeliveryTimesForFittingRequest(
            pairedUserId = pairedUserId,
            basketLineIds = products.map { it.id },
            fittingType = fittingType,
            kittingType = KittingType.LOGISTIC,
            deliveryType = DeliveryType.LOGISTIC,
            address = address,
            addressComment = addressComment
        )
        val deliveryTimes = handleResponseResult {
            networkService.fittingsDeliveryTimes(deliveryTimesRequest)
        }.getOrThrow()

        return deliveryTimes.fittingConfirmationData(
            addresses = addresses,
            selectedProducts = products
        )
    }

    override suspend fun loadExistingFittingConfirmationData(
        products: List<CartProduct>,
        deliveryId: String,
        fittingType: FittingType,
        clientAddress: ClientDeliveryAddress?
    ): FittingConfirmationData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return FittingConfirmationData()
        }

        val addressRequest = GetListClientAddressForCheckoutRequest(
            pairedUserId = pairedUserId
        )
        val addresses = handleResponseResult {
            networkService.clientAddressCheckout(addressRequest)
        }.getOrThrow()
        val address = when (fittingType) {
            FittingType.NONE -> addresses.boutiqueAddress?.address
            FittingType.IN_THE_STORE -> addresses.boutiqueAddress?.address
            FittingType.AT_HOME -> clientAddress?.address ?: addresses.clientAddress?.address
        }
        val addressComment = when (fittingType) {
            FittingType.NONE -> ""
            FittingType.IN_THE_STORE -> ""
            FittingType.AT_HOME -> clientAddress?.comment ?: addresses.clientAddress?.comment.orEmpty()
        }
        val deliveryTimesRequest = GetDeliveryIntervalsForExistingFittingRequest(
            pairedUserId = pairedUserId,
            deliveryId = deliveryId,
            fittingLineIds = products.map { product -> product.id },
            fittingType = fittingType,
            kittingType = KittingType.LOGISTIC,
            deliveryType = DeliveryType.LOGISTIC,
            address = address,
            addressComment = addressComment
        )
        val deliveryTimes = handleResponseResult {
            networkService.fittingsDeliveryTimesForExisingDelivery(deliveryTimesRequest)
        }.getOrThrow()

        return deliveryTimes.fittingConfirmationData(
            addresses = addresses,
            deliveryId = deliveryId,
            selectedProducts = products
        )
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
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            throw ConfirmFittingException("Не удалось определить клиента")
        }

        val addressRequest = GetListClientAddressForCheckoutRequest(
            pairedUserId = pairedUserId
        )
        val addresses = handleResponseResult {
            networkService.clientAddressCheckout(addressRequest)
        }.getOrThrow()
        val address = when (fittingType) {
            FittingType.NONE -> addresses.boutiqueAddress?.address
            FittingType.IN_THE_STORE -> addresses.boutiqueAddress?.address
            FittingType.AT_HOME -> clientAddress?.address ?: addresses.clientAddress?.address
        }.orEmpty()
        val addressComment = when (fittingType) {
            FittingType.NONE -> ""
            FittingType.IN_THE_STORE -> ""
            FittingType.AT_HOME -> clientAddress?.comment ?: addresses.clientAddress?.comment.orEmpty()
        }
        val lines = when {
            useSingleDelivery -> {
                products.map { product ->
                    TransferBasketToFittingLineResponse(
                        lineId = product.id,
                        deliveryTime = singleInterval?.deliveryTimeDto
                    )
                }
            }
            else -> {
                deliveryGroups.flatMap { group ->
                    val interval = group.intervals.firstOrNull { interval ->
                        interval.id == selectedDeliveryIntervalIds[group.id]
                    }
                    group.products.map { product ->
                        TransferBasketToFittingLineResponse(
                            lineId = product.id,
                            deliveryTime = interval?.deliveryTimeDto
                        )
                    }
                }
            }
        }
        val request = TransferBasketToFittingRequest(
            pairedUserId = pairedUserId,
            lines = lines,
            address = address,
            addressComment = addressComment,
            fittingType = fittingType,
            deliveryType = DeliveryType.LOGISTIC,
            kittingType = KittingType.LOGISTIC
        )
        val result = FittingConfirmationResult(
            deliveryLines = when {
                useSingleDelivery -> {
                    listOf(
                        FittingConfirmationResultDeliveryLine(
                            intervalSummary = singleInterval?.summary.orEmpty(),
                            productsCount = products.sumOf { it.quantity }
                        )
                    )
                }
                else -> {
                    deliveryGroups.map { group ->
                        val interval = group.intervals.firstOrNull { interval ->
                            interval.id == selectedDeliveryIntervalIds[group.id]
                        }
                        FittingConfirmationResultDeliveryLine(
                            intervalSummary = interval?.summary.orEmpty(),
                            productsCount = group.products.sumOf { it.quantity }
                        )
                    }
                }
            },
            address = address
        )

        handleResponse(
            request = { networkService.fittingsTransferFromBasket(request) },
            onSuccess = {},
            onEmpty = {},
            onFailure = { error -> throw ConfirmFittingException(error.message) }
        )

        return result
    }

    override suspend fun loadClientDeliveryAddresses(): List<ClientDeliveryAddress> {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
        if (clientId.isEmpty()) {
            return emptyList()
        }

        val request = GetListClientAddressRequest(
            clientId = clientId
        )
        val result = handleResponseResult {
            networkService.clientAddressList(request)
        }.getOrThrow()

        return result.items.orEmpty().mapNotNull { address -> address.clientDeliveryAddress }
    }

    override suspend fun searchClientDeliveryAddress(
        query: String
    ): List<ClientDeliveryAddressSuggestion> {
        if (query.isBlank()) {
            return emptyList()
        }

        val result = handleResponseResult {
            networkService.addressSuggestion(
                limit = 10,
                searchText = query
            )
        }.getOrThrow()

        return result.items.orEmpty().mapNotNull { suggestion ->
            suggestion.clientDeliveryAddressSuggestion
        }
    }

    override suspend fun createClientDeliveryAddress(
        address: ClientDeliveryAddress
    ): ClientDeliveryAddress {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
        if (clientId.isEmpty()) {
            throw ClientAddressException("Не удалось определить клиента")
        }

        val request = CreateClientAddressRequest(
            clientId = clientId,
            address = address.clientAddressBaseDto,
            coordinate = address.coordinateDto
        )
        val result = handleResponseResult {
            networkService.clientAddressCreate(request)
        }.getOrThrow()

        return result.clientDeliveryAddress(address)
    }

    override suspend fun updateClientDeliveryAddress(
        address: ClientDeliveryAddress
    ): ClientDeliveryAddress {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
        if (clientId.isEmpty()) {
            throw ClientAddressException("Не удалось определить клиента")
        }

        val request = UpdateClientAddressRequest(
            clientId = clientId,
            address = address.clientAddressDto,
            coordinate = address.coordinateDto
        )
        val result = handleResponseResult {
            networkService.clientAddressUpdate(request)
        }.getOrThrow()

        return result.clientDeliveryAddress(address)
    }

    override suspend fun deleteClientDeliveryAddress(addressId: Int) {
        val clientId = settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
        if (clientId.isEmpty()) {
            throw ClientAddressException("Не удалось определить клиента")
        }

        val request = DeleteClientAddressRequest(
            clientId = clientId,
            addressId = addressId
        )
        handleResponse(
            request = { networkService.clientAddressDelete(request) },
            onSuccess = {},
            onFailure = { error -> throw ClientAddressException(error.message) }
        )
    }

    override suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        cartProductDao.updateIsForPayment(product.id, paySwitch)

        handleResponse(
            request = {
                val request = product.paySwitchRequest(pairedUserId, paySwitch)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error ->
                loadBasket()
                throw ChangePaySwitchException(error.message)
            }
        )
    }

    override suspend fun changeFittingPaySwitch(product: CartProduct, paySwitch: Boolean) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.fittingPaySwitchRequest(pairedUserId, paySwitch)
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw ChangeFittingPaySwitchException(error.message) }
        )
    }

    override suspend fun addProductToBasket(productId: String, sizeId: String?) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val product = catalogFilterProductsDao.select(productId)
            ?: catalogViewHistoryProductDao.selectCatalogProduct(productId)
            ?: throw AddProductToBasketException(ADD_PRODUCT_TO_BASKET_ERROR_MESSAGE)

        handleResponse(
            request = {
                val request = product.addProductToBasketRequest(pairedUserId, sizeId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw AddProductToBasketException(error.message) }
        )
    }

    override suspend fun addProductToBasket(product: CatalogFilterProductsEntity, sizeId: String?) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.addProductToBasketRequest(pairedUserId, sizeId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw AddProductToBasketException(error.message) }
        )
    }

    override suspend fun setProductSize(product: CartProduct, sizeId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.changeSizeRequest(pairedUserId, sizeId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw SetProductSizeException(error.message) }
        )
    }

    override suspend fun addProductSize(product: CartProduct, sizeId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.addProductSizeRequest(pairedUserId, sizeId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw AddProductSizeException(error.message) }
        )
    }

    override suspend fun removeProductSize(product: CartProduct, size: CartProductSize) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.removeProductSizeRequest(pairedUserId, size)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw RemoveProductSizeException(error.message) }
        )
    }

    override suspend fun setProductColor(product: CartProduct, colorId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.changeColorRequest(pairedUserId, colorId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw SetProductColorException(error.message) }
        )
    }

    override suspend fun setProductQuantity(product: CartProduct, quantity: Int) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.changeQuantityRequest(pairedUserId, quantity)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw SetProductQuantityException(error.message) }
        )
    }

    override suspend fun fittingReturnProduct(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = FittingOperationRequest(
                    pairedUserId = pairedUserId,
                    items = listOf(
                        fittingOperationJson.encodeToJsonElement(
                            FittingReturnProductOperationRequestItemResponse(
                                operationType = FittingOperationRequestType.RETURN_PRODUCT,
                                operationOrder = 0,
                                lineId = product.id
                            )
                        )
                    )
                )
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw FittingReturnProductException(error.message) }
        )
    }

    override suspend fun setFittingProductSize(product: CartProduct, sizeId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val delivery = fittingDeliveryForProduct(pairedUserId, product)
                val request = FittingOperationRequest(
                    pairedUserId = pairedUserId,
                    items = listOf(
                        fittingOperationJson.encodeToJsonElement(
                            FittingChangeLineSizeOperationRequestItemResponse(
                                operationType = FittingOperationRequestType.CHANGE_LINE_SIZE,
                                operationOrder = 0,
                                lineId = product.id,
                                sizeId = sizeId,
                                deliveryTime = delivery?.deliveryTime,
                                address = delivery?.address,
                                addressComment = delivery?.addressComment,
                                fittingType = delivery?.fittingType,
                                deliveryType = delivery?.deliveryType,
                                kittingType = delivery?.kittingType,
                                leaveItemsToClient = delivery?.leaveItemsToClient,
                                isGoWithCourier = delivery?.isGoWithCourier,
                                packOrderMyself = delivery?.packOrderMyself,
                                manageClientCommunicationsMyself = delivery?.manageClientCommunicationsMyself,
                                deliveryComment = delivery?.deliveryComment
                            )
                        )
                    )
                )
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw ChangeFittingLineSizeException(error.message) }
        )
    }

    override suspend fun setFittingProductColor(product: CartProduct, colorId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val delivery = fittingDeliveryForProduct(pairedUserId, product)
                val request = FittingOperationRequest(
                    pairedUserId = pairedUserId,
                    items = listOf(
                        fittingOperationJson.encodeToJsonElement(
                            FittingChangeLineColorOperationRequestItemResponse(
                                operationType = FittingOperationRequestType.CHANGE_LINE_COLOR,
                                operationOrder = 0,
                                lineId = product.id,
                                colorId = colorId,
                                deliveryTime = delivery?.deliveryTime,
                                address = delivery?.address,
                                addressComment = delivery?.addressComment,
                                fittingType = delivery?.fittingType,
                                deliveryType = delivery?.deliveryType,
                                kittingType = delivery?.kittingType,
                                leaveItemsToClient = delivery?.leaveItemsToClient,
                                isGoWithCourier = delivery?.isGoWithCourier,
                                packOrderMyself = delivery?.packOrderMyself,
                                manageClientCommunicationsMyself = delivery?.manageClientCommunicationsMyself,
                                deliveryComment = delivery?.deliveryComment
                            )
                        )
                    )
                )
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw ChangeFittingLineColorException(error.message) }
        )
    }

    private suspend fun fittingDeliveryForProduct(
        pairedUserId: String,
        product: CartProduct
    ): FittingDeliveryResponse? {
        return handleResponseResult {
            networkService.fittingsByPairedUserId(pairedUserId)
        }.getOrNull()
            ?.deliveries
            .orEmpty()
            .firstOrNull { delivery ->
                delivery.lines.orEmpty().any { line ->
                    line.lineId == product.id
                }
            }
    }

    override suspend fun loadAvailableSizes(product: CartProduct): ProductAvailableSizesEntity {
        if (product.itemId.isEmpty() || product.colorId.isEmpty()) {
            return ProductAvailableSizesEntity(
                items = emptyList(),
                countryCode = null,
                sizeTableTitle = null,
                sizeTableUrl = null
            )
        }

        val response = handleResponseResult {
            networkService.catalogAvailableSizes(
                AvailableSizesRequest(
                    itemId = product.itemId,
                    colorId = product.colorId
                )
            )
        }.getOrThrow()

        return ProductAvailableSizesEntity(
            items = response.items.orEmpty().mapNotNull { size ->
                val sizeId = size.sizeId ?: return@mapNotNull null
                ProductAvailableSizeEntity(
                    sizeId = sizeId,
                    russianSize = size.sizeFullName,
                    sizeFullName = size.sizeFullName,
                    inStock = size.inStock == true
                )
            },
            countryCode = null,
            sizeTableTitle = response.sizeTableTitle,
            sizeTableUrl = response.sizeTableUrl
        )
    }

    override suspend fun loadAvailableColors(product: CartProduct): List<ProductAvailableColor> {
        if (product.itemId.isEmpty() || product.sizeId.isEmpty()) {
            return emptyList()
        }

        val response = handleResponseResult {
            networkService.catalogAvailableColors(
                AvailableColorsRequest(
                    itemId = product.itemId,
                    sizeId = product.sizeId
                )
            )
        }.getOrThrow()

        return response.items.orEmpty().mapNotNull { color ->
            val colorId = color.colorId ?: return@mapNotNull null
            ProductAvailableColor(
                id = colorId,
                name = color.colorFullName.orEmpty().ifBlank { colorId },
                hex = color.colorHex.orEmpty(),
                selected = colorId == product.colorId
            )
        }
    }

    override suspend fun deleteProduct(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.deleteProductRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw DeleteProductException(error.message) }
        )
    }

    override suspend fun deleteLook(lookId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = deleteLookRequest(lookId, pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw DeleteLookException(error.message) }
        )
    }

    override suspend fun disassembleLook(products: List<CartProduct>) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = disassembleLookRequest(products, pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw DisassembleLookException(error.message) }
        )
    }

    override suspend fun moveProductsAfterDrag(products: List<CartProduct>) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val currentProducts = cartProductDao.selectAll().map { it.cartProduct }
        val changedLookProducts = products.filter { product ->
            currentProducts.firstOrNull { it.id == product.id }?.lookId != product.lookId
        }
        appDatabase.withTransaction {
            cartProductDao.upsert(
                products.mapIndexed { index, product ->
                    product.entity(index)
                }
            )
        }

        handleResponse(
            request = {
                val request = cartProductsAfterDragRequest(
                    products = products,
                    changedLookProducts = changedLookProducts,
                    pairedUserId = pairedUserId
                )
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error ->
                loadBasket()
                throw MoveProductsAfterDragException(error.message)
            }
        )
    }

    override suspend fun removeAlternative(alternative: CartProductAlternative) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = alternative.removeAlternativeRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw RemoveAlternativeException(error.message) }
        )
    }

    override suspend fun switchProductWithAlternative(alternative: CartProductAlternative) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = alternative.switchProductWithAlternativeRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw SwitchProductWithAlternativeException(error.message) }
        )
    }

    override suspend fun basketHideAlternatives(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        cartProductDao.updateIsAlternativesPaletteOpen(product.id, false)
        handleResponse(
            request = {
                val request = product.basketHideAlternativesRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onFailure = { error -> throw BasketHideAlternativesException(error.message) }
        )
    }

    override suspend fun basketShowAlternatives(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        cartProductDao.updateIsAlternativesPaletteOpen(product.id, true)
        handleResponse(
            request = {
                val request = product.basketShowAlternativesRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onFailure = { error -> throw BasketShowAlternativesException(error.message) }
        )
    }

    override suspend fun basketReturnOriginal(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.basketReturnOriginalRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasket() },
            onEmpty = { loadBasket() },
            onFailure = { error -> throw BasketReturnOriginalException(error.message) }
        )
    }

    override suspend fun cartBadge(): Int {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return 0
        }

        val counters = handleResponseResult {
            networkService.activityCountersByPairedUserId(pairedUserId)
        }.getOrThrow()

        return counters.items.orEmpty()
            .firstOrNull { it.type == ActivityCounterType.BASKET }
            ?.value ?: 0
    }
}
