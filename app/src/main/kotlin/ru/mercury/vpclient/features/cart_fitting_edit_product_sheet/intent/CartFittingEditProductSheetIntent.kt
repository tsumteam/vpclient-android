package ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingEditProductSheetIntent: Intent {
    data object ChangeColorClick: CartFittingEditProductSheetIntent
    data object ChangeSizeClick: CartFittingEditProductSheetIntent
    data object ConfirmClick: CartFittingEditProductSheetIntent
    data object DismissRequest: CartFittingEditProductSheetIntent
}
