package ru.mercury.vpclient.features.cart

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.event.CartEvent
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val productInteractor: ProductInteractor
): ClientViewModel<CartIntent, CartModel, CartEvent>(CartModel()) {

    init {
        dispatch(CartIntent.CollectCart)
        dispatch(CartIntent.LoadCart)
    }

    override fun dispatch(intent: CartIntent) {
        when (intent) {
            is CartIntent.CollectCart -> {
                launch {
                    cartInteractor.cartProductsFlow.collectLatest { products ->
                        reduce { it.copy(products = products) }
                    }
                }
            }
            is CartIntent.LoadCart -> launch { cartInteractor.loadBasket() }
            is CartIntent.PullToRefresh -> {
                if (stateFlow.value.isRefreshing) return
                reduce { it.copy(isRefreshing = true) }
                launch {
                    cartInteractor.loadBasket()
                    dispatch(CartIntent.RefreshCompleted)
                }
            }
            is CartIntent.RefreshCompleted -> reduce { it.copy(isRefreshing = false) }
            is CartIntent.CloseClick -> launch { MainEventManager.send(BackRoute) }
            is CartIntent.ProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true)) }
            }
            is CartIntent.ChangePaySwitch -> {
                when {
                    !intent.product.isForPayment && intent.product.size.isBlank() && intent.paySwitch -> {
                        reduce { it.copy(selectSizeProduct = intent.product) }
                    }
                    else -> {
                        stateFlow.value.paySwitchJob?.cancel()
                        val paySwitchJob = launch {
                            try {
                                cartInteractor.changePaySwitch(intent.product, intent.paySwitch)
                            } finally {
                                reduce { it.copy(paySwitchJob = null) }
                            }
                        }
                        reduce { it.copy(paySwitchJob = paySwitchJob) }
                    }
                }
            }
            is CartIntent.SelectSizeClick -> {
                reduce { it.copy(selectSizeProduct = null) }
                dispatch(CartIntent.ShowSizePicker(intent.product))
            }
            is CartIntent.HideSelectSizeDialog -> reduce { it.copy(selectSizeProduct = null) }
            is CartIntent.ShowSizePicker -> {
                if (intent.product.detailId.isEmpty()) return
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = intent.product,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerJob = null
                    )
                }
                val sizePickerJob = launch {
                    launch { productInteractor.loadProduct(intent.product.detailId) }
                    productInteractor.productFlow(intent.product.detailId).collectLatest { entity ->
                        entity.availableSizes?.let { sizes ->
                            reduce { it.copy(sizePickerSizes = sizes) }
                        }
                    }
                }
                reduce { it.copy(sizePickerJob = sizePickerJob) }
            }
            is CartIntent.HideSizePicker -> {
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = null,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerJob = null
                    )
                }
            }
            is CartIntent.ToggleSizePickerItem -> {
                val sizeId = stateFlow.value.sizePickerSizes?.items?.getOrNull(intent.index)?.sizeId
                reduce { it.copy(sizePickerSelectedId = sizeId) }
            }
            is CartIntent.ConfirmSizePicker -> {
                val product = stateFlow.value.sizePickerProduct ?: return
                val sizeId = stateFlow.value.sizePickerSelectedId ?: return
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = null,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerJob = null
                    )
                }
                launch { cartInteractor.setProductSize(product, sizeId) }
            }
            is CartIntent.AlternativeClick -> {
                launch { cartInteractor.switchProductWithAlternative(intent.alternative) }
            }
            is CartIntent.RemoveAlternativeClick -> {
                launch { cartInteractor.removeAlternative(intent.alternative) }
            }
            is CartIntent.HideAlternativesClick -> {
                launch { cartInteractor.hideAlternatives(intent.product) }
            }
            is CartIntent.SelectPayMode -> reduce { it.copy(payMode = intent.mode) }
            is CartIntent.SelectViewMode -> reduce { it.copy(viewMode = intent.mode) }
            is CartIntent.ChatClick,
            is CartIntent.FittingClick,
            is CartIntent.BuyClick -> return
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            is ClientException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }
}
