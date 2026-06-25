package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SalesPerMonthResponse(
    @SerialName("connectedBrandSalesAmount") val connectedBrandSalesAmount: Double? = null,
    @SerialName("connectedBrandSalesInMonthTotalSalesPercent") val connectedBrandSalesInMonthTotalSalesPercent: Double? = null,
    @SerialName("otherBrandSalesAmount") val otherBrandSalesAmount: Double? = null,
    @SerialName("otherBrandSalesInMonthTotalSalesPercent") val otherBrandSalesInMonthTotalSalesPercent: Double? = null,
    @SerialName("totalSalesAmount") val totalSalesAmount: Double? = null,
    @SerialName("commonSalesPayrollPartAmount") val commonSalesPayrollPartAmount: Double? = null,
    @SerialName("personalSalesPayrollPartAmount") val personalSalesPayrollPartAmount: Double? = null,
    @SerialName("totalPayrollAmount") val totalPayrollAmount: Double? = null
)
