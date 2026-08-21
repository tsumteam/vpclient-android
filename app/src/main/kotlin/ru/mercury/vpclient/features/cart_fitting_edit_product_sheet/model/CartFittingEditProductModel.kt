package ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.model

import ru.mercury.vpclient.shared.mvi.Model

data class CartFittingEditProductModel(
    val isSizeSelectionAvailable: Boolean
): Model
