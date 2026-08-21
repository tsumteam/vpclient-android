package ru.mercury.vpclient.features.cart_fitting_sheet.intent

import ru.mercury.vpclient.shared.data.entity.CartFittingSheetOption
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingIntent: Intent {
    data object DismissClick: CartFittingIntent
    data class ConfirmClick(val option: CartFittingSheetOption): CartFittingIntent
}
