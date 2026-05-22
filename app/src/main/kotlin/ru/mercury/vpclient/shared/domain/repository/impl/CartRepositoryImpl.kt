package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.error.AddProductToBasketException
import ru.mercury.vpclient.shared.data.error.BasketHideAlternativesException
import ru.mercury.vpclient.shared.data.error.BasketReturnOriginalException
import ru.mercury.vpclient.shared.data.error.BasketShowAlternativesException
import ru.mercury.vpclient.shared.data.error.ChangePaySwitchException
import ru.mercury.vpclient.shared.data.error.DeleteLookException
import ru.mercury.vpclient.shared.data.error.DeleteProductException
import ru.mercury.vpclient.shared.data.error.DisassembleLookException
import ru.mercury.vpclient.shared.data.error.RemoveAlternativeException
import ru.mercury.vpclient.shared.data.error.SetProductSizeException
import ru.mercury.vpclient.shared.data.error.SwitchProductWithAlternativeException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.entity.ActivityCounterTypeRequestEnum
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.addProductToBasketRequest
import ru.mercury.vpclient.shared.domain.mapper.basketHideAlternativesRequest
import ru.mercury.vpclient.shared.domain.mapper.basketReturnOriginalRequest
import ru.mercury.vpclient.shared.domain.mapper.basketShowAlternativesRequest
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import ru.mercury.vpclient.shared.domain.mapper.cartProductWithLook
import ru.mercury.vpclient.shared.domain.mapper.catalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.changeSizeRequest
import ru.mercury.vpclient.shared.domain.mapper.deleteLookRequest
import ru.mercury.vpclient.shared.domain.mapper.deleteProductRequest
import ru.mercury.vpclient.shared.domain.mapper.disassembleLookRequest
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.paySwitchRequest
import ru.mercury.vpclient.shared.domain.mapper.removeAlternativeRequest
import ru.mercury.vpclient.shared.domain.mapper.switchProductWithAlternativeRequest
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

private const val ADD_PRODUCT_TO_BASKET_ERROR_MESSAGE = "Не удалось добавить товар в корзину"

class CartRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val cartProductDao: CartProductDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val settingsDataStore: SettingsDataStore
): CartRepository {

    override val cartProductsFlow: Flow<List<CartProduct>> = cartProductDao.selectAllFlow()
        .map { entities -> entities.map { it.cartProduct } }

    override val cartSize: Flow<Int> = cartProductDao.cartSizeFlow()

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

    override suspend fun addProductToBasket(productId: String, sizeId: String?) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val product = catalogFilterProductsDao.select(productId)
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
            .firstOrNull { it.type == ActivityCounterTypeRequestEnum.BASKET }
            ?.value ?: 0
    }
}
