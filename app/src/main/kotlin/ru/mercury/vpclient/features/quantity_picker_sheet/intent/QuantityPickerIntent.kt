package ru.mercury.vpclient.features.quantity_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface QuantityPickerIntent: Intent {
    data object ConfirmClick: QuantityPickerIntent
    data object DismissClick: QuantityPickerIntent
    data class QuantityClick(val index: Int): QuantityPickerIntent
}
