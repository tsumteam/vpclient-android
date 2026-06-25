package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonalSalesResponse(
    @SerialName("weekCaption") val weekCaption: String? = null,
    @SerialName("weekPeriodCaption") val weekPeriodCaption: String? = null,
    @SerialName("connectedBrandSales") val connectedBrandSales: Double? = null,
    @SerialName("otherBrandSales") val otherBrandSales: Double? = null,
    @SerialName("goalSalesAmount") val goalSalesAmount: Double? = null,
    @SerialName("goalSalesAmountMinimum") val goalSalesAmountMinimum: Double? = null,
    @SerialName("completedWeekPlansInARowQty") val completedWeekPlansInARowQty: Int? = null,
    @SerialName("connectedBrandSalesAmount") val connectedBrandSalesAmount: Double? = null,
    @SerialName("connectedBrandSalesInWeekTotalSalesPercent") val connectedBrandSalesInWeekTotalSalesPercent: Double? = null,
    @SerialName("otherBrandSalesAmount") val otherBrandSalesAmount: Double? = null,
    @SerialName("otherBrandSalesInWeekTotalSalesPercent") val otherBrandSalesInWeekTotalSalesPercent: Double? = null,
    @SerialName("totalSalesAmount") val totalSalesAmount: Double? = null,
    @SerialName("leftForPlan") val leftForPlan: Double? = null,
    @SerialName("personalSalesPayrollPartAmount") val personalSalesPayrollPartAmount: Double? = null,
    @SerialName("quarterPayrollAmount") val quarterPayrollAmount: Double? = null,
    @SerialName("personalSalesIncreasingCoefficient") val personalSalesIncreasingCoefficient: Double? = null,
    @SerialName("personalSalesGoalPercentCompletion") val personalSalesGoalPercentCompletion: Double? = null,
    @SerialName("personalSalesQuarter") val personalSalesQuarter: Double? = null
)
