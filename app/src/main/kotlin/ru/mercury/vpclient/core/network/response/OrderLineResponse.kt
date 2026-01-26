@file:Suppress("unused")

package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderLineResponse(
    @SerialName("amount") val amount: Double?,
    @SerialName("amountDisc") val amountDisc: Double?,
    @SerialName("amountWithDisc") val amountWithDisc: Double?,
    @SerialName("barcode") val barcode: String?,
    @SerialName("deliveryStatusEmployee") val deliveryStatusEmployee: String?,
    @SerialName("itemColor") val itemColor: String?,
    @SerialName("itemId") val itemId: String?,
    @SerialName("itemName") val itemName: String?,
    @SerialName("itemSize") val itemSize: String?,
    @SerialName("lineId") val lineId: String?,
    @SerialName("maxDiscAmount") val maxDiscAmount: Double?,
    @SerialName("nameAlias") val nameAlias: String?,
    @SerialName("price") val price: Double?,
    @SerialName("priceWithDisc") val priceWithDisc: Double?,
    @SerialName("qty") val qty: Int?,
    @SerialName("submarkName") val submarkName: String?,
    @SerialName("switchPay") val switchPay: Int?,
    @SerialName("vat") val vat: String?
) {
    companion object {
        const val DELIVERY_STATUS_EMPLOYEE_VPCLIENT_WAIT = "vpclientWait"
        const val DELIVERY_STATUS_EMPLOYEE_RETURN_FROM_CUSTOMER = "returnFromCustomer"
    }
}
