package ru.mercury.vpclient.features.cart.intent

import ru.mercury.vpclient.features.cart.model.CartPayMode
import ru.mercury.vpclient.features.cart.model.CartViewMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartIntent: Intent {
    data object CollectCart: CartIntent
    data object LoadCart: CartIntent
    data object CloseClick: CartIntent
    data object ChatClick: CartIntent
    data object FittingClick: CartIntent
    data object BuyClick: CartIntent
    data object HideSelectSizeDialog: CartIntent
    data object HideSizePicker: CartIntent
    data object ConfirmSizePicker: CartIntent
    data class ProductClick(val id: String): CartIntent
    data class ChangePaySwitch(val product: CartProduct, val paySwitch: Boolean): CartIntent
    data class ShowSizePicker(val product: CartProduct): CartIntent
    data class SelectSizeClick(val product: CartProduct): CartIntent
    data class AlternativeClick(val alternative: CartProductAlternative): CartIntent
    data class RemoveAlternativeClick(val alternative: CartProductAlternative): CartIntent
    data class HideAlternativesClick(val product: CartProduct): CartIntent
    data class ToggleSizePickerItem(val index: Int): CartIntent
    data class SelectPayMode(val mode: CartPayMode): CartIntent
    data class SelectViewMode(val mode: CartViewMode): CartIntent
}
