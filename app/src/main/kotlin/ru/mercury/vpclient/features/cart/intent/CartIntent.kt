package ru.mercury.vpclient.features.cart.intent

import ru.mercury.vpclient.features.cart_edit_product_sheet.intent.CartEditProductIntent
import ru.mercury.vpclient.features.cart_empty_order_dialog.intent.CartEmptyOrderIntent
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent.CartFittingEditProductIntent
import ru.mercury.vpclient.features.cart_fitting_empty_order_dialog.intent.CartFittingEmptyOrderIntent
import ru.mercury.vpclient.features.cart_fitting_sheet.intent.CartFittingIntent
import ru.mercury.vpclient.features.color_picker_sheet.intent.ColorPickerIntent
import ru.mercury.vpclient.features.messenger_sheet.intent.MessengerIntent
import ru.mercury.vpclient.features.quantity_picker_sheet.intent.QuantityPickerIntent
import ru.mercury.vpclient.features.size_picker_sheet.intent.SizePickerIntent
import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.mvi.Intent
import ru.mercury.vpclient.shared.ui.components.fitting.FittingDeliveryHeaderState

sealed interface CartIntent: Intent {
    data object CollectCart: CartIntent
    data object CollectFittingCount: CartIntent
    data object CollectActiveEmployee: CartIntent
    data object LoadCurrentUser: CartIntent
    data object LoadCart: CartIntent
    data object LoadFitting: CartIntent
    data object PullToRefresh: CartIntent
    data object RefreshCompleted: CartIntent
    data object CloseClick: CartIntent
    data object ChatClick: CartIntent
    data object FittingClick: CartIntent
    data object FittingTabClick: CartIntent
    data object BuyClick: CartIntent
    data object FittingBuyClick: CartIntent
    data object DismissColorPickerSheet: CartIntent
    data object DismissQuantityPickerSheet: CartIntent
    data object ShowFittingProductsSheet: CartIntent
    data object DismissFittingProductsSheet: CartIntent
    data object CollectInitialPage: CartIntent
    data class OnCartEditProductIntent(val intent: CartEditProductIntent): CartIntent
    data class OnCartFittingEditProductIntent(val intent: CartFittingEditProductIntent): CartIntent
    data class OnCartFittingIntent(val intent: CartFittingIntent): CartIntent
    data class OnSizePickerIntent(val intent: SizePickerIntent): CartIntent
    data class OnColorPickerIntent(val intent: ColorPickerIntent): CartIntent
    data class OnQuantityPickerIntent(val intent: QuantityPickerIntent): CartIntent
    data class OnCartEmptyOrderIntent(val intent: CartEmptyOrderIntent): CartIntent
    data class OnCartFittingEmptyOrderIntent(val intent: CartFittingEmptyOrderIntent): CartIntent
    data class OnMessengerSheetIntent(val intent: MessengerIntent): CartIntent
    data class FittingDeliveryClick(val productIds: List<String>, val deliveryId: String, val fittingType: FittingType, val header: FittingDeliveryHeaderState): CartIntent
    data class ProductClick(val id: String): CartIntent
    data class ChangePaySwitch(val product: CartProduct, val paySwitch: Boolean): CartIntent
    data class ChangeFittingPaySwitch(val product: CartProduct, val paySwitch: Boolean): CartIntent
    data class ShowSizePicker(val product: CartProduct, val addSize: Boolean = false): CartIntent
    data class ShowColorPicker(val product: CartProduct, val forFitting: Boolean = false): CartIntent
    data class ShowQuantityPicker(val product: CartProduct): CartIntent
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
    data class SelectPayMode(val mode: CartPayMode): CartIntent
    data class MoveProductAfterDrag(val productId: String, val targetProductId: String, val placeAfterTarget: Boolean): CartIntent
    data class ConfirmFittingProductsSheet(val productIds: List<String>): CartIntent
}
