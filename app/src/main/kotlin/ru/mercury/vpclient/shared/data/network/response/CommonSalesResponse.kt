package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonSalesResponse(
    @SerialName("weekCaption") val weekCaption: String? = null,
    @SerialName("weekPeriodCaption") val weekPeriodCaption: String? = null,
    @SerialName("btqWeekTotalSalesByPlan") val btqWeekTotalSalesByPlan: Double? = null,
    @SerialName("btqWeekPlan") val btqWeekPlan: Double? = null,
    @SerialName("commonSalesPayrollPercent") val commonSalesPayrollPercent: Double? = null,
    @SerialName("btqWeekTotalSalesAmount") val btqWeekTotalSalesAmount: Double? = null,
    @SerialName("btqWeekLeftForPlan") val btqWeekLeftForPlan: Double? = null,
    @SerialName("daysForPlan") val daysForPlan: Int? = null,
    @SerialName("daysWorkedWithForecast") val daysWorkedWithForecast: Int? = null,
    @SerialName("commonSalesPayrollPartAmount") val commonSalesPayrollPartAmount: Double? = null
)
