package ru.mercury.vpclient.features.quantity_picker_sheet.model

import ru.mercury.vpclient.shared.data.entity.CartQuantityItem
import ru.mercury.vpclient.shared.mvi.Model

data class QuantityPickerModel(
    val quantities: List<CartQuantityItem>
): Model {

    val hasSelectedQuantity: Boolean
        get() = quantities.any { it.selected }

    val selectedIndex: Int
        get() = quantities.indexOfFirst { it.selected }.coerceAtLeast(0)
}
