package ru.mercury.vpclient.features.cart

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val productInteractor: ProductInteractor
): ClientViewModel<CartIntent, CartModel, Event>(CartModel()) {

    private var paySwitchJob: Job? = null
    private var sizePickerJob: Job? = null

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
            is CartIntent.CloseClick -> launch { MainEventManager.send(BackRoute) }
            is CartIntent.ProductClick -> {
                if (intent.id.isEmpty()) return
                launch { MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true)) }
            }
            is CartIntent.ChangePaySwitch -> {
                when {
                    !intent.product.isForPayment && intent.product.size.isBlank() && intent.paySwitch -> {
                        reduce { it.copy(selectSizeProduct = intent.product) }
                    }
                    else -> {
                        paySwitchJob?.cancel()
                        paySwitchJob = launch { cartInteractor.changePaySwitch(intent.product, intent.paySwitch) }
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
                sizePickerJob?.cancel()
                reduce { it.copy(sizePickerProduct = intent.product, sizePickerSizes = null, sizePickerSelectedId = null) }
                sizePickerJob = launch {
                    launch { productInteractor.loadProduct(intent.product.detailId) }
                    productInteractor.productFlow(intent.product.detailId).collectLatest { entity ->
                        entity.availableSizes?.let { sizes ->
                            reduce { it.copy(sizePickerSizes = sizes) }
                        }
                    }
                }
            }
            is CartIntent.HideSizePicker -> {
                sizePickerJob?.cancel()
                reduce { it.copy(sizePickerProduct = null, sizePickerSizes = null, sizePickerSelectedId = null) }
            }
            is CartIntent.ToggleSizePickerItem -> {
                val sizeId = stateFlow.value.sizePickerSizes?.items?.getOrNull(intent.index)?.sizeId
                reduce { it.copy(sizePickerSelectedId = sizeId) }
            }
            is CartIntent.ConfirmSizePicker -> {
                val product = stateFlow.value.sizePickerProduct ?: return
                val sizeId = stateFlow.value.sizePickerSelectedId ?: return
                sizePickerJob?.cancel()
                reduce { it.copy(sizePickerProduct = null, sizePickerSizes = null, sizePickerSelectedId = null) }
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
}
