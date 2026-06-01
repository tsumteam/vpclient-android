package ru.mercury.vpclient.features.cart_fitting_sheet.model

data class CartFittingSheetModel(
    val clientName: String,
    val clientFeminine: Boolean,
    val allProductsCount: Int,
    val allProductsSummary: String,
    val paymentProductsCount: Int,
    val paymentProductsSummary: String,
    val hasProductsWithoutSize: Boolean
)
