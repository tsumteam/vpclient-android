package ru.mercury.vpclient.features.color_picker_sheet.model

import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.mvi.Model

data class ColorPickerModel(
    val colors: List<ProductAvailableColor>
): Model {

    val hasSelectedColor: Boolean
        get() = colors.any { it.selected }

    val selectedIndex: Int
        get() = colors.indexOfFirst { it.selected }.coerceAtLeast(0)
}
