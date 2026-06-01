package ru.mercury.vpclient.features.cart_size_picker_sheet.model

import ru.mercury.vpclient.features.cart_size_picker_sheet.SizeSelectorState

data class CartSizePickerSheetModel(
    val sizeSelectorState: SizeSelectorState,
    val buttonText: String
)
