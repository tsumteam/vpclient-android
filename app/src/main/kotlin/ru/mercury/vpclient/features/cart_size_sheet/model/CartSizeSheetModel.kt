package ru.mercury.vpclient.features.cart_size_sheet.model

import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState

data class CartSizeSheetModel(
    val sizeSelectorState: SizeSelectorState,
    val buttonText: String
)
