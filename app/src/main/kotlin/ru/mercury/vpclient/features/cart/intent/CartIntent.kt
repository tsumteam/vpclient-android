package ru.mercury.vpclient.features.cart.intent

import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum
import ru.mercury.vpclient.shared.mvi.Intent
import ru.mercury.vpclient.shared.ui.components.fitting.FittingDeliveryHeaderState

sealed interface CartIntent: Intent {
    data object CollectCart: CartIntent
    data object CollectActiveEmployee: CartIntent
    data object LoadCurrentUser: CartIntent
    data object LoadActiveEmployee: CartIntent
    data object LoadCart: CartIntent
    data object LoadFitting: CartIntent
    data object PullToRefresh: CartIntent
    data object RefreshCompleted: CartIntent
    data object CloseClick: CartIntent
    data object ChatClick: CartIntent
    data object FittingClick: CartIntent
    data object FittingTabClick: CartIntent
    data object BuyClick: CartIntent
    data object HideFittingSheet: CartIntent
    data object ShowFittingProductsSheet: CartIntent
    data object HideFittingProductsSheet: CartIntent
    data object HideSelectSizeDialog: CartIntent
    data object HideSizePicker: CartIntent
    data object HideEditProductSheet: CartIntent
    data object HideFittingEditProductSheet: CartIntent
    data object HideColorPicker: CartIntent
    data object HideQuantityPicker: CartIntent
    data object ConfirmSizePicker: CartIntent
    data object ConfirmColorPicker: CartIntent
    data object ConfirmQuantityPicker: CartIntent
    data object CollectInitialPage: CartIntent
    data class FittingDeliveryClick(
        val productIds: List<String>,
        val deliveryId: String,
        val fittingType: FittingTypeDtoEnum,
        val header: FittingDeliveryHeaderState
    ): CartIntent
    data class ProductClick(val id: String): CartIntent
    data class ChangePaySwitch(val product: CartProduct, val paySwitch: Boolean): CartIntent
    data class ChangeFittingPaySwitch(val product: CartProduct, val paySwitch: Boolean): CartIntent
    data class ShowSizePicker(val product: CartProduct, val addSize: Boolean = false): CartIntent
    data class ShowFittingSizePicker(val product: CartProduct): CartIntent
    data class ShowColorPicker(val product: CartProduct, val forFitting: Boolean = false): CartIntent
    data class ShowQuantityPicker(val product: CartProduct): CartIntent
    data class SelectSizeClick(val product: CartProduct): CartIntent
    data class AlternativeClick(val alternative: CartProductAlternative): CartIntent
    data class RemoveAlternativeClick(val alternative: CartProductAlternative): CartIntent
    data class HideAlternativesClick(val product: CartProduct): CartIntent
    data class EditProductSwipeClick(val product: CartProduct): CartIntent
    data class EditFittingProductSwipeClick(val product: CartProduct): CartIntent
    data class ReturnFittingProductToBasketSwipeClick(val product: CartProduct): CartIntent
    data class AddSizeClick(val product: CartProduct): CartIntent
    data class ChangeQuantityClick(val product: CartProduct): CartIntent
    data class ChangeColorClick(val product: CartProduct): CartIntent
    data class RemoveProductSizeClick(val product: CartProduct, val size: CartProductSize): CartIntent
    data class DeleteProductSwipeClick(val product: CartProduct): CartIntent
    data class DetachProductFromLookSwipeClick(val product: CartProduct): CartIntent
    data class ReturnOriginalSwipeClick(val product: CartProduct): CartIntent
    data class ShowAlternativesSwipeClick(val product: CartProduct): CartIntent
    data class HideAlternativesSwipeClick(val product: CartProduct): CartIntent
    data class DisassembleLookSwipeClick(val lookId: String): CartIntent
    data class DeleteLookSwipeClick(val lookId: String): CartIntent
    data class ToggleSizePickerItem(val index: Int): CartIntent
    data class ToggleColorPickerItem(val index: Int): CartIntent
    data class ToggleQuantityPickerItem(val index: Int): CartIntent
    data class SelectPayMode(val mode: CartPayMode): CartIntent
    data class MoveProductAfterDrag(val productId: String, val targetProductId: String, val placeAfterTarget: Boolean): CartIntent
    data class ConfirmFittingSheet(val productIds: List<String>): CartIntent
    data class ConfirmFittingProductsSheet(val productIds: List<String>): CartIntent
}
