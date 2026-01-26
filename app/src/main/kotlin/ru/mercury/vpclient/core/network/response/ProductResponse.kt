@file:Suppress("unused")

package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("barcode") val barcode: String?,
    @SerialName("cis") val cis: String?,
    @SerialName("itemColorId") val itemColorId: String?,
    @SerialName("itemId") val itemId: String?,
    @SerialName("itemName") val itemName: String?,
    @SerialName("itemSizeId") val itemSizeId: String?,
    @SerialName("lineId") val lineId: String?,
    @SerialName("price") val price: Double?,
    @SerialName("priceWithDisc") val priceWithDisc: Double?,
    @SerialName("quantity") val quantity: Int?,
    @SerialName("status") val status: String?,
    @SerialName("vat") val vat: String?,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("imageUrls") val imageUrls: List<String>?
) {
    companion object {
        const val STATUS_CREATED = "Created"                     // Создано. Такого статуса на мобилке не будет
        const val STATUS_APPROVED = "Approved"                   // Согласовано. Такого статуса на мобилке не будет
        const val STATUS_PACKING = "Packing"                     // На комплектации. Такого статуса на мобилке не будет
        const val STATUS_READY = "Ready"                         // Готово. После сканирования ГМ переходит в Shipped. Такого статуса на мобилке не будет
        const val STATUS_SHIPPED = "Shipped"                     // Отправлено клиенту (в доставке у водителя)
        const val STATUS_DELIVERED = "Delivered"                 // Доставлено клиенту
        const val STATUS_RETURN = "Return"                       // К возврату
        const val STATUS_RETURNED = "Returned"                   // Возвращено от клиента
        const val STATUS_SOLD = "Sold"                           // Продано
        const val STATUS_CANCELED = "Canceled"                   // Отменено
        const val STATUS_REGRADING = "Regrading"                 // Пересорт
        const val STATUS_TRANSFER_CANCELED = "TransferCanceled"  // Отказано
        const val STATUS_RETURNED_FROM_HALL = "ReturnedFromHall" // Возвращено из зала
    }
}
