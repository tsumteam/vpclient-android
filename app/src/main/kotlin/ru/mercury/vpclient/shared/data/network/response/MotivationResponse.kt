package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MotivationResponse(
    @SerialName("personal") val personal: PersonalSalesResponse? = null,
    @SerialName("common") val common: CommonSalesResponse? = null,
    @SerialName("byBrand") val byBrand: SalesByBrandResponse? = null,
    @SerialName("perMonth") val perMonth: SalesPerMonthResponse? = null,
    @SerialName("grade") val grade: GradeResponse? = null
)
