package ru.mercury.vpclient.features.cart_fitting_sheet.model

import ru.mercury.vpclient.shared.mvi.Model

data class CartFittingModel(
    val clientName: String,
    val clientFeminine: Boolean,
    val allProductsCount: Int,
    val allProductsSummary: String,
    val paymentProductsCount: Int,
    val paymentProductsSummary: String,
    val hasProductsWithoutSize: Boolean
): Model
