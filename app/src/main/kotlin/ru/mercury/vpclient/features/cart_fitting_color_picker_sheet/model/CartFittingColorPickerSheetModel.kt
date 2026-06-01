package ru.mercury.vpclient.features.cart_fitting_color_picker_sheet.model

import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor

data class CartFittingColorPickerSheetModel(
    val colors: List<ProductAvailableColor>
) {
    val hasSelectedColor: Boolean
        get() {
            return colors.any { it.selected }
        }
}
