package ru.mercury.vpclient.shared.data.entity

data class ProfileOrder(
    val id: Int,
    val orderNumber: String,
    val amount: String,
    val isFinished: Boolean,
    val statusPrefix: String,
    val statusDescription: String,
    val imageUrls: List<String>,
    val productsCount: Int,
    val showPaymentBadge: Boolean,
    val isReceipt: Boolean
)
