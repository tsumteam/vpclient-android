package ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingEditProductIntent: Intent {
    data object ChangeColorClick: CartFittingEditProductIntent
    data object ChangeSizeClick: CartFittingEditProductIntent
    data object DismissClick: CartFittingEditProductIntent
}
