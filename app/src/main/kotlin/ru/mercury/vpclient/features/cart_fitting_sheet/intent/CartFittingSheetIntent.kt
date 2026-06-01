package ru.mercury.vpclient.features.cart_fitting_sheet.intent

import ru.mercury.vpclient.shared.data.entity.CartFittingSheetOption
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingSheetIntent: Intent {
    data class ConfirmClick(val option: CartFittingSheetOption): CartFittingSheetIntent
    data object DismissRequest: CartFittingSheetIntent
}
